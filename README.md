# GIS Tax Refactoring Workspace

This repository contains the source code and reference projects for refactoring the GIS/Tax application.

---

## 🚀 Quick Start for Team

### 1. Clone the Repository

```bash
# Using SSH
git clone git@github.com:theniswara/gis-tax-refactoring.git

# Or using HTTPS
git clone https://github.com/theniswara/gis-tax-refactoring.git

cd gis-tax-refactoring
```

### 2. Get Latest Updates (if already cloned)

```bash
git pull origin main
```

---

## 🖥️ Run Frontend

```bash
cd leaflet-geo-FE

# Install dependencies (IMPORTANT: use --legacy-peer-deps!)
npm install --legacy-peer-deps

# Run the app
npm run start

# Open browser: http://localhost:4200
```

---

## ⚙️ Run Backend

```bash
cd leaflet-geo

# Run the app
./mvnw spring-boot:run

# API available at: http://localhost:8080
```

---

## 📁 Project Structure

```
.
├── docs/                        # Implementation guides (READ FIRST!)
│   ├── frontend-implementation-plan.md
│   └── backend-implementation-plan.md
│
├── leaflet-geo-FE/             # Frontend - Source (being cleaned)
├── university-frontend/         # Frontend - Reference (clean patterns)
│
├── leaflet-geo/                # Backend - Source (being cleaned)
└── university-backend/          # Backend - Reference (clean patterns)
```

---

## 📊 Project Status

| Project | Type | Status | Description |
|---------|------|--------|-------------|
| `leaflet-geo-FE` | Angular 18 | 🔧 Refactoring | GIS/Tax frontend with Leaflet maps |
| `university-frontend` | Angular | ✅ Reference | Clean code patterns |
| `leaflet-geo` | Spring Boot | 🔧 Refactoring | Backend API with PostgreSQL/Oracle |
| `university-backend` | Spring Boot | ✅ Reference | Clean code patterns |

---

## 📖 For Team Members

1. **Read the implementation plans** in `docs/` folder
2. **Follow the phases** sequentially  
3. **Create a new branch** before making changes
4. **Test after each phase** before moving to the next
5. **Create Pull Request** when done

---

## 🔧 Troubleshooting

### Frontend: npm install fails
Use this command instead:
```bash
npm install --legacy-peer-deps
```

### Backend: mvnw not executable
```bash
chmod +x mvnw
./mvnw spring-boot:run
```

### Pull latest changes
```bash
git pull origin main
```
