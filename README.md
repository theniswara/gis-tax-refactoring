# GIS Tax Refactoring Workspace

This repository contains the source code and reference projects for refactoring the GIS/Tax application.

## Structure

```
.
├── docs/                        # Implementation guides
│   ├── frontend-implementation-plan.md
│   └── backend-implementation-plan.md
│
├── leaflet-geo-FE/             # Frontend - Source (being cleaned)
├── university-frontend/         # Frontend - Reference (clean patterns)
│
├── leaflet-geo/                # Backend - Source (being cleaned)
└── university-backend/          # Backend - Reference (clean patterns)
```

## Quick Reference

| Project | Type | Status | Description |
|---------|------|--------|-------------|
| `leaflet-geo-FE` | Angular | 🔧 Refactoring | GIS/Tax frontend with Leaflet maps |
| `university-frontend` | Angular | ✅ Reference | Clean code patterns |
| `leaflet-geo` | Spring Boot | 🔧 Refactoring | Backend API with PostgreSQL/Oracle |
| `university-backend` | Spring Boot | ✅ Reference | Clean code patterns |

## For Team Members

1. **Read the implementation plans** in `docs/` folder
2. **Follow the phases** sequentially
3. **Test after each phase** before moving to the next

## Commands

### Frontend
```bash
cd leaflet-geo-FE
npm install
npm run start
```

### Backend
```bash
cd leaflet-geo
./mvnw spring-boot:run
```
