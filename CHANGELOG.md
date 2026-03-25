# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [0.3.22] - 2026-03-25

### Added
- Comprehensive test suite covering all major packages:
  - All 37 enum classes with factory method, toString, round-trip, and edge case tests
  - Model/entity classes (Account, Domain, ColorData, Screenshot, Group, Animation, etc.)
  - Extended model classes (Label, LatLng, SimpleElement, SimplePage, Template, etc.)
  - All 16 exception classes with message and HTTP status annotation verification
  - All DTO classes (AuditUpdateDto, Subscription, PageBuiltMessage, UXIssueReportDto, etc.)
  - Audit model hierarchy (Audit, AuditRecord, PageAuditRecord, DomainAuditRecord, Score, etc.)
  - Audit message and recommendation classes
  - Audit statistics classes
  - Rule classes and RuleFactory with all rule type builds
  - Journey model classes (Journey, SimpleStep, LoginStep, LandingStep, Redirect)
  - Message classes for inter-service communication
  - Browsing package classes (Coordinates, ElementNode, ValueDomain, FormField)
  - Competitive analysis and design system model classes
  - Mapper classes (Body, Base64 deserializers)
  - Configuration property classes (LookseeCoreProperties, PusherProperties, SeleniumProperties)
  - VS Code plugin classes (Tree, TreeNode, SessionTestTracker, TestMapper)
  - Utility classes (AuditUtils, ColorUtils, ColorPaletteUtils, JourneyUtils, ListUtils)
  - Service layer tests with Mockito mocking (AuditService)

### Changed
- Updated README.md with current version (0.3.22), comprehensive test documentation, and project structure
- Updated CONTRIBUTING.md with development guidelines, testing instructions, and code standards
- Updated CHANGELOG.md with project history

## [0.3.21] - Previous

### Added
- Expanded utility and enum test coverage
- Enum factory and timing utility unit tests

## [0.3.0] - Earlier

### Added
- Spring Boot auto-configuration for repositories and services
- Modular Pusher configuration with fallback support
- Conditional Pub/Sub publisher creation
- Selenium WebDriver configuration with properties
- MessageBroadcaster guaranteed availability pattern

### Fixed
- PageService.saveForUser scoped to user-specific records
- PageService.findLatestInsight null-safe returns
- PageService.addPageState guards against missing pages
- Removed debug System.out.println statements
