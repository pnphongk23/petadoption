<!-- filepath: e:\Git\hanoipetadoption\.github\memory-bank\modeConfig.md -->
# Mode Configuration

This document defines the operational modes for the Memory Bank system and their specific configurations.

## Available Modes

### Architect Mode [ARCHITECT]
**Purpose**: System design and architecture planning

#### Capabilities
- Memory Bank initialization and updates
- Architecture decisions and documentation
- System planning and component design

#### Focus Areas
- Component relationships
- Architectural patterns
- System-wide decisions

#### Files Accessed
- productContext.md
- decisionLog.md
- systemPatterns.md

### Code Mode [CODE]
**Purpose**: Implementation and coding

#### Capabilities
- Code generation and modification
- Implementation of features
- Bug fixes and optimizations

#### Focus Areas
- Feature implementation
- Code quality
- Unit testing

#### Files Accessed
- activeContext.md
- systemPatterns.md
- progress.md

### Ask Mode [ASK]
**Purpose**: Information and guidance

#### Capabilities
- Context understanding
- Documentation assistance
- Best practices guidance

#### Focus Areas
- Knowledge sharing
- Documentation
- Clarifications

#### Files Accessed
- All Memory Bank files as needed

### Debug Mode [DEBUG]
**Purpose**: Troubleshooting and problem-solving

#### Capabilities
- System behavior analysis
- Incremental testing
- Root cause identification

#### Focus Areas
- Bug resolution
- Performance optimization
- Diagnostic investigation

#### Files Accessed
- activeContext.md
- systemPatterns.md
- decisionLog.md

## Mode Switching Triggers

### Intent-Based Triggers
- **Code Mode**: implement, create, build, code, develop, fix
- **Debug Mode**: debug, troubleshoot, diagnose, investigate, analyze, trace
- **Architect Mode**: design, architect, structure, plan
- **Ask Mode**: explain, help, what, how, why

### Command-Based Triggers
- **[CODE]**: Switch to Code Mode
- **[ARCHITECT]**: Switch to Architect Mode
- **[ASK]**: Switch to Ask Mode
- **[DEBUG]**: Switch to Debug Mode

## Default Mode
The default mode is **Ask Mode** [ASK] when no specific mode is indicated.
