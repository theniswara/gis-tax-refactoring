# Frontend Refactoring Guide: leaflet-geo-FE

> **👥 For**: Frontend Developer  
> **🎯 Goal**: Clean up `leaflet-geo-FE` following `university-frontend` patterns  
> **⏱️ Estimated Time**: 2-3 days

---

## ⚠️ IMPORTANT: Before You Start

1. **ALWAYS work on a separate branch**
   ```bash
   git checkout -b refactor/code-cleanup
   ```

2. **NEVER push directly to main/master**

3. **Test after EVERY phase** before moving to next

4. **If something breaks**, revert immediately:
   ```bash
   git checkout .  # Undo all changes
   # OR
   git stash       # Save changes temporarily
   ```

---

## 📋 Project Overview

| What | Path |
|------|------|
| **Source (Clean this)** | `leaflet-geo-FE/` |
| **Reference (Copy patterns from here)** | `university-frontend/` |

---

## Phase 1: Documentation Cleanup ✅ (DONE)

**Status**: Already completed. Markdown files moved to `docs/` folder.

---

## Phase 2: Add Barrel Files (index.ts)

**What**: Create `index.ts` files for cleaner imports.  
**Risk**: ⚪ None - just adding new files.

### Step-by-Step

1. **Create barrel file for core services**:
   ```bash
   cd leaflet-geo-FE/src/app/core/services
   ```

2. **Create `index.ts`**:
   ```typescript
   // src/app/core/services/index.ts
   export * from './auth.service';
   export * from './rest-api.service';
   export * from './bidang.service';
   export * from './bprd-api.service';
   export * from './csrf.service';
   export * from './event.service';
   export * from './language.service';
   export * from './master.service';
   export * from './pendapatan.service';
   export * from './remote-config.service';
   export * from './translation-sync.service';
   ```

3. **Test**:
   ```bash
   ng build --configuration development
   ```

4. **Commit**:
   ```bash
   git add .
   git commit -m "feat: add barrel files for core services"
   ```

### ✅ Checklist
- [ ] Created `core/services/index.ts`
- [ ] Build passes
- [ ] Committed changes

---

## Phase 3: Consolidate Services

**What**: Move services from `shared/services/` to `core/services/`.  
**Risk**: 🟠 Medium - import paths will change.

### Before Starting
```bash
# Check what services are in shared/services
ls src/app/shared/services/
```

### Step-by-Step

1. **For EACH service file in `shared/services/`**:
   - Move file to `core/services/`
   - Update the import paths in ALL files that use it
   - Test build

2. **How to find files that import a service**:
   ```bash
   # Example: find all files importing 'some-service'
   grep -r "from.*shared/services/some-service" src/
   ```

3. **Update imports**:
   - **Before**: `import { X } from '../../shared/services/x.service';`
   - **After**: `import { X } from '../../core/services/x.service';`

4. **Test after EACH service move**:
   ```bash
   ng build --configuration development
   ```

5. **If build fails**, revert immediately:
   ```bash
   git checkout .
   ```

### ✅ Checklist
- [ ] Moved all services from shared/services → core/services
- [ ] Updated all import paths
- [ ] Build passes
- [ ] App runs correctly (`npm run start`)
- [ ] Committed changes

---

## Phase 4: Add Module Comments

**What**: Add section comments like university-frontend.  
**Risk**: ⚪ None - just adding comments.

### Example (app.module.ts)
```typescript
@NgModule({
  declarations: [
    AppComponent,
    
    // ============================================
    // LAYOUT COMPONENTS
    // ============================================
    // (layout components here)
    
    // ============================================
    // PAGE COMPONENTS  
    // ============================================
    // (page components here)
  ],
  // ...
})
```

### ✅ Checklist
- [ ] Added comments to app.module.ts
- [ ] Added comments to other modules as needed
- [ ] Committed changes

---

## Phase 5: Code Quality

**What**: Clean up imports, add JSDoc.  
**Risk**: ⚪ None.

### Tasks
1. Remove unused imports (use IDE "Organize Imports")
2. Add JSDoc to services:
   ```typescript
   /**
    * Service for handling authentication operations.
    * Manages login, logout, and token refresh.
    */
   @Injectable({ providedIn: 'root' })
   export class AuthService { ... }
   ```

---

## 🔥 Emergency Rollback

If anything goes wrong:

```bash
# Option 1: Undo all uncommitted changes
git checkout .

# Option 2: Go back to last commit
git reset --hard HEAD

# Option 3: Go back to main branch
git checkout main
```

---

## ✅ Final Verification Checklist

Before merging your branch:

- [ ] `ng build --configuration development` passes
- [ ] `npm run start` works
- [ ] Login functionality works
- [ ] Map/Leaflet loads correctly
- [ ] Dashboard pages load
- [ ] No console errors
