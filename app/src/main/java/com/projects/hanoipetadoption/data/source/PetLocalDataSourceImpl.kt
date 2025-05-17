package com.projects.hanoipetadoption.data.source

import com.projects.hanoipetadoption.data.dao.PetAdoptionRequirementDao
import com.projects.hanoipetadoption.data.dao.PetCharacteristicDao
import com.projects.hanoipetadoption.data.dao.PetDao
import com.projects.hanoipetadoption.data.dao.PetHealthStatusDao
import com.projects.hanoipetadoption.data.mapper.toCharacteristicEntities
import com.projects.hanoipetadoption.data.mapper.toHealthStatusEntities
import com.projects.hanoipetadoption.data.mapper.toPetData
import com.projects.hanoipetadoption.data.mapper.toPetEntity
import com.projects.hanoipetadoption.data.mapper.toRequirementEntities
import com.projects.hanoipetadoption.data.model.PetData
import com.projects.hanoipetadoption.data.model.PetWithAdoptionStatusEntity
import com.projects.hanoipetadoption.ui.model.SamplePetsData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Implementation of PetLocalDataSource that uses Room Database
 */
class PetLocalDataSourceImpl(
    private val petDao: PetDao,
    private val characteristicDao: PetCharacteristicDao,
    private val healthStatusDao: PetHealthStatusDao,
    private val requirementDao: PetAdoptionRequirementDao
) : PetLocalDataSource {

    override suspend fun getPetById(id: String): PetData? = withContext(Dispatchers.IO) {
        val petEntity = petDao.getPetById(id) ?: return@withContext null
        val characteristics = characteristicDao.getCharacteristicsForPet(id).map { it.characteristic }
        val healthStatus = healthStatusDao.getHealthStatusForPet(id).associate { it.statusName to it.statusValue }
        val requirements = requirementDao.getRequirementsForPet(id).map { it.requirement }
        
        return@withContext petEntity.toPetData(characteristics, healthStatus, requirements)
    }
    
    override suspend fun insertPet(petData: PetData) = withContext(Dispatchers.IO) {
        // Insert pet entity
        petDao.insertPet(petData.toPetEntity())
        
        // Delete and re-insert related data
        deleteRelatedData(petData.id)
        insertRelatedData(petData)
    }
    
    override suspend fun insertPets(petDataList: List<PetData>) = withContext(Dispatchers.IO) {
        // Insert all pet entities
        petDao.insertPets(petDataList.map { it.toPetEntity() })
        
        // Insert all related data
        petDataList.forEach { petData ->
            // First delete any existing related data
            deleteRelatedData(petData.id)
            // Then insert new related data
            insertRelatedData(petData)
        }
    }
    
    override suspend fun updatePet(petData: PetData) = withContext(Dispatchers.IO) {
        // Update pet entity
        petDao.updatePet(petData.toPetEntity())
        
        // Delete and re-insert related data
        deleteRelatedData(petData.id)
        insertRelatedData(petData)
    }

    override suspend fun toggleFavorite(id: String): Boolean = withContext(Dispatchers.IO) {
        val rowsUpdated = petDao.toggleFavorite(id)
        return@withContext rowsUpdated > 0
    }

    override suspend fun getAllPets(): List<PetData> = withContext(Dispatchers.IO) {
        val pets = petDao.getAllPets()
        return@withContext pets.map { petEntity ->
            val characteristics = characteristicDao.getCharacteristicsForPet(petEntity.id).map { it.characteristic }
            val healthStatus = healthStatusDao.getHealthStatusForPet(petEntity.id).associate { it.statusName to it.statusValue }
            val requirements = requirementDao.getRequirementsForPet(petEntity.id).map { it.requirement }
            
            petEntity.toPetData(characteristics, healthStatus, requirements)
        }
    }
    
    override suspend fun deletePet(id: String) = withContext(Dispatchers.IO) {
        // Delete related data (will cascade due to foreign key constraints)
        petDao.deletePetById(id)
    }
    
    /**
     * Gets all pets with their adoption status using a JOIN query
     * @return Flow of list of pet entities with adoption status
     */
    override fun getAllPetsWithAdoptionStatus(): Flow<List<PetWithAdoptionStatusEntity>> {
        return petDao.getAllPetsWithAdoptionStatus()
    }
    
    private suspend fun deleteRelatedData(petId: String) {
        characteristicDao.deleteCharacteristicsForPet(petId)
        healthStatusDao.deleteHealthStatusForPet(petId)
        requirementDao.deleteRequirementsForPet(petId)
    }
    
    private suspend fun insertRelatedData(petData: PetData) {
        characteristicDao.insertCharacteristics(petData.toCharacteristicEntities())
        healthStatusDao.insertHealthStatus(petData.toHealthStatusEntities())
        requirementDao.insertRequirements(petData.toRequirementEntities())
    }
    
    /**
     * Initialize the database with sample data
     */
    suspend fun initializeWithSampleDataIfEmpty() = withContext(Dispatchers.IO) {
        val existingPets = petDao.getAllPets()
        if (existingPets.isEmpty()) {
            val samplePets = SamplePetsData.getPets().map { pet ->
                PetData(
                    id = pet.id,
                    name = pet.name,
                    category = pet.breed,
                    breed = pet.breed,
                    age = pet.age,
                    gender = pet.gender,
                    description = pet.description,
                    imageRes = pet.imageRes,
                    isFavorite = pet.isFavorite,
                    characteristics = pet.characteristics,
                    weight = "",
                    healthStatus = mapOf(
                        "Tiêm phòng" to false, 
                        "Triệt sản" to true,
                        "Tẩy giun" to true,
                        "Kiểm tra sức khỏe tổng quát" to true
                    ),
                    adoptionRequirements = listOf(
                        "Có không gian sống phù hợp",
                        "Cam kết chăm sóc thú cưng đầy đủ",
                        "Được phép nuôi thú cưng tại nơi ở",
                        "Đồng ý kiểm tra định kỳ sau khi nhận nuôi"
                    )
                )
            }
            
            insertPets(samplePets)
        }
    }
}
