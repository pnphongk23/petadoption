package com.projects.hanoipetadoption.ui.model

object SamplePetsData {
    fun getPets(): List<Pet> {
       return listOf(
            Pet(
                id = "1",
                name = "Kiki",
                breed = "Poodle",
                age = "Nhỡ",
                gender = PetGender.MALE,
                description = "Thân thiện với con người",
                imageRes = "https://www.hanoipetadoption.com/admin/user-content/Animal/5205e4a9-cbc9-422f-8a5d-72d88f8dcd26.jpeg",
                isFavorite = false,
                characteristics = listOf("Thân thiện với con người")
            ),
            Pet(
                id = "2",
                name = "Tôm",
                breed = "Mèo ta",
                age = "Nhí",
                gender = PetGender.MALE,
                description = "Hơi nhút nhát",
                imageRes = "https://www.hanoipetadoption.com/admin/user-content/Animal/d449764e-651d-4e28-967e-0c8e0baf353b.jpg",
                isFavorite = false,
                characteristics = listOf("Hơi nhút nhát")
            ),
            Pet(
                id = "3",
                name = "Dừa",
                breed = "Mèo Ta",
                age = "Nhí",
                gender = PetGender.FEMALE,
                description = "Ngoan ngoãn, chăm ăn",
                imageRes = "https://www.hanoipetadoption.com/admin/user-content/Animal/e735235c-9774-4ab5-8f09-9646ec2abfab.jpeg",
                isFavorite = false,
                characteristics = listOf("Ngoan ngoãn", "chăm ăn")
            ),
            Pet(
                id = "4",
                name = "Minnie",
                breed = "Mèo Ta",
                age = "Nhỡ",
                gender = PetGender.FEMALE,
                description = "Quấn người, hay đòi bế",
                imageRes = "https://www.hanoipetadoption.com/admin/user-content/Animal/e735db89-12b3-4595-b67d-f57e4e4adf7c.jpeg",
                isFavorite = false,
                characteristics = listOf("Quấn người", "hay đòi bế")
            ),
            Pet(
                id = "5",
                name = "Bún",
                breed = "Poodle",
                age = "Nhỡ",
                gender = PetGender.FEMALE,
                description = "Ngoan ngoãn, dễ bảo",
                imageRes = "https://www.hanoipetadoption.com/admin/user-content/Animal/049df93e-8b86-4a69-aacd-8c706125b24e.jpeg",
                isFavorite = false,
                characteristics = listOf("Ngoan ngoãn", "dễ bảo")
            ),
            Pet(
                id = "6",
                name = "Kem",
                breed = "Mèo Tây",
                age = "Nhí",
                gender = PetGender.FEMALE,
                description = "Nhút nhát, chăm ăn",
                imageRes = "https://www.hanoipetadoption.com/admin/user-content/Animal/fbdff515-3bf2-41d6-b016-90f5c8ecce62.jpeg",
                isFavorite = false,
                characteristics = listOf("Nhút nhát", "chăm ăn")
            ),
            Pet(
                id = "7",
                name = "Táo",
                breed = "Chó Ta",
                age = "Nhỡ",
                gender = PetGender.MALE,
                description = "Thích được đi dạo",
                imageRes = "https://www.hanoipetadoption.com/admin/user-content/Animal/7fa47a3b-5868-4191-8118-a976916a71b4.jpeg",
                isFavorite = false,
                characteristics = listOf("Thích được đi dạo")
            ),
            Pet(
                id = "8",
                name = "Best",
                breed = "Poodle",
                age = "Nhỡ",
                gender = PetGender.MALE,
                description = "Quấn người, thích được bế",
                imageRes = "https://www.hanoipetadoption.com/admin/user-content/Animal/8380ade3-7ae6-40ff-84b7-b62f8bd27a52.jpg",
                isFavorite = false,
                characteristics = listOf("Quấn người", "thích được bế")
            ),
            Pet(
                id = "9",
                name = "Bơ",
                breed = "Mèo Tây",
                age = "Nhỡ",
                gender = PetGender.MALE,
                description = "Đi vệ sinh đúng chỗ, ham ăn",
                imageRes = "https://www.hanoipetadoption.com/admin/user-content/Animal/e4eca057-b931-4e8c-b4d8-e0e9ce5dff3b.jpeg",
                isFavorite = false,
                characteristics = listOf("Đi vệ sinh đúng chỗ", "ham ăn")
            ),
            Pet(
                id = "10",
                name = "Pepsi",
                breed = "Mèo Ta",
                age = "Nhỡ",
                gender = PetGender.MALE,
                description = "Thích vận động",
                imageRes = "https://www.hanoipetadoption.com/admin/user-content/Animal/b2078130-e14f-4da2-967b-e9b770238881.jpeg",
                isFavorite = false,
                characteristics = listOf("Thích vận động")
            ),
            Pet(
                id = "11",
                name = "Moa Moa",
                breed = "Cún Lai",
                age = "Nhỡ",
                gender = PetGender.FEMALE,
                description = "Hơi nhút nhát",
                imageRes = "https://www.hanoipetadoption.com/admin/user-content/Animal/5f153284-396d-41c8-aa3c-cf7274388c52.jpg",
                isFavorite = false,
                characteristics = listOf("Hơi nhút nhát")
            ),
            Pet(
                id = "12",
                name = "Soda",
                breed = "Mèo Tây",
                age = "Nhỡ",
                gender = PetGender.FEMALE,
                description = "Ngủ ngày nhiều, ít vận động",
                imageRes = "https://www.hanoipetadoption.com/admin/user-content/Animal/fcad1dae-1e55-4b64-b359-54855c7af4b2.jpeg",
                isFavorite = false,
                characteristics = listOf("Ngủ ngày nhiều", "ít vận động")
            ),
            Pet(
                id = "13",
                name = "Susu",
                breed = "Chó Ta",
                age = "Trưởng thành",
                gender = PetGender.MALE,
                description = "Hơi khó gần với người lạ",
                imageRes = "https://www.hanoipetadoption.com/admin/user-content/Animal/b538cab3-ec37-4b77-a293-c644398876ad.jpeg",
                isFavorite = false,
                characteristics = listOf("Hơi khó gần với người lạ")
            ),
            Pet(
                id = "14",
                name = "Captain",
                breed = "Lạp xưởng",
                age = "Trưởng thành",
                gender = PetGender.MALE,
                description = "Thân thiện, dễ bảo",
                imageRes = "https://www.hanoipetadoption.com/admin/user-content/Animal/cb7f38dd-6e01-4154-8a82-72f1b21d2b25.jpeg",
                isFavorite = false,
                characteristics = listOf("Thân thiện", "dễ bảo")
            ),
            Pet(
                id = "15",
                name = "Củ cải",
                breed = "Mèo Ta",
                age = "Trưởng thành",
                gender = PetGender.FEMALE,
                description = "Nhút nhát, chăm ăn",
                imageRes = "https://www.hanoipetadoption.com/admin/user-content/Animal/48eea3fe-e0aa-4eec-a720-1148ed6827f7.jpeg",
                isFavorite = false,
                characteristics = listOf("Nhút nhát", "chăm ăn")
            ),
            Pet(
                id = "16",
                name = "Tuyết",
                breed = "Mèo Ta",
                age = "Nhỡ",
                gender = PetGender.MALE,
                description = "Thân thiện, chăm ăn",
                imageRes = "https://www.hanoipetadoption.com/admin/user-content/Animal/5537f035-106c-4d87-a144-2ef3dc718b92.jpeg",
                isFavorite = false,
                characteristics = listOf("Thân thiện", "chăm ăn")
            ),
            Pet(
                id = "17",
                name = "Mây",
                breed = "Mèo Tây",
                age = "Nhỡ",
                gender = PetGender.MALE,
                description = "Thân thiện, chăm ăn, ít vận động",
                imageRes = "https://www.hanoipetadoption.com/admin/user-content/Animal/fc7401d8-a087-4985-8b5e-5efbc3b8e128.jpeg",
                isFavorite = false,
                characteristics = listOf("Thân thiện", "chăm ăn", "ít vận động")
            ),
            Pet(
                id = "18",
                name = "Xúc xích",
                breed = "Mèo Ta",
                age = "Trưởng thành",
                gender = PetGender.FEMALE,
                description = "Nhút nhát, chăm ăn",
                imageRes = "https://www.hanoipetadoption.com/admin/user-content/Animal/57bcc55b-d3ea-4144-9e11-c06660ba27ac.jpeg",
                isFavorite = false,
                characteristics = listOf("Nhút nhát", "chăm ăn")
            ),
            Pet(
                id = "19",
                name = "Tiger",
                breed = "Mèo Ta",
                age = "Nhỡ",
                gender = PetGender.MALE,
                description = "Thân thiện, chăm ăn, thích được chơi cùng",
                imageRes = "https://www.hanoipetadoption.com/admin/user-content/Animal/889cb06f-c4d2-4f87-8fab-0b976651b531.jpeg",
                isFavorite = false,
                characteristics = listOf("Thân thiện", "chăm ăn", "thích được chơi cùng")
            ),
            Pet(
                id = "20",
                name = "Leo",
                breed = "Chó Ta",
                age = "Nhỡ",
                gender = PetGender.MALE,
                description = "Ngoan ngoãn, dễ bảo",
                imageRes = "https://www.hanoipetadoption.com/admin/user-content/Animal/75c3e0c1-239d-4216-8f02-ce485891b8a6.jpeg ",
                isFavorite = false,
                characteristics = listOf("Ngoan ngoãn", "dễ bảo")
            ),
        )
    }
}