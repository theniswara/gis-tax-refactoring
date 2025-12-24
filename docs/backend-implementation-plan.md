# Backend Refactoring Guide: leaflet-geo

> **👥 For**: Backend Developer (You)  
> **🎯 Goal**: Clean up `leaflet-geo` following `university-backend` patterns  
> **⏱️ Estimated Time**: 2-3 days

---

## ⚠️ IMPORTANT: Before You Start

1. **ALWAYS work on a separate branch**
   ```bash
   git checkout -b refactor/code-cleanup
   ```

2. **NEVER push directly to main/master**

3. **Test after EVERY phase**:
   ```bash
   ./mvnw clean compile
   ./mvnw spring-boot:run
   ```

4. **If something breaks**, revert immediately:
   ```bash
   git checkout .
   ```

---

## 📋 Project Overview

| What | Path |
|------|------|
| **Source (Clean this)** | `leaflet-geo/` |
| **Reference (Copy patterns from here)** | `university-backend/` |

---

## Phase 1: Documentation Cleanup

**What**: Move markdown files to `docs/` folder.  
**Risk**: ⚪ None.

### Step-by-Step

```bash
cd leaflet-geo

# Create docs folder
mkdir -p docs

# Move markdown files
mv DASHBOARD_PENDAPATAN_QUICKSTART.md docs/
mv DATABASE_SETUP.md docs/
mv DAT_OBJEK_PAJAK_API.md docs/
mv MULTIPLE_DATABASE_SETUP.md docs/
mv SIMATDA_TARGET_REALISASI_QUERIES.md docs/
mv WKB_TO_GEOJSON_CONVERSION.md docs/

# Commit
git add .
git commit -m "docs: organize markdown files into docs folder"
```

### ✅ Checklist
- [ ] Created `docs/` folder
- [ ] Moved all .md files (except README.md)
- [ ] Committed changes

---

## Phase 2: Rename entity → model

**What**: Rename package from `entity/` to `model/` for consistency.  
**Risk**: 🟡 Low - but requires updating imports.

### Step-by-Step

1. **Rename the folder**:
   ```bash
   cd src/main/java/com/example/leaflet_geo
   mv entity model
   ```

2. **Update package declaration in EACH file in model/**:
   
   Open each file and change:
   ```java
   // BEFORE
   package com.example.leaflet_geo.entity;
   
   // AFTER
   package com.example.leaflet_geo.model;
   ```
   
   Files to update:
   - `model/Bidang.java`
   - `model/DatObjekPajak.java`
   - `model/DatSubjekPajak.java`
   - `model/KecamatanWithCount.java`
   - `model/KelurahanWithCount.java`
   - `model/RefKecamatan.java`
   - `model/RefKelurahan.java`

3. **Update imports in ALL other files**:
   
   Use IDE "Find and Replace" or:
   ```bash
   # Find all files with old import
   grep -r "com.example.leaflet_geo.entity" src/
   ```
   
   Change:
   ```java
   // BEFORE
   import com.example.leaflet_geo.entity.Bidang;
   
   // AFTER
   import com.example.leaflet_geo.model.Bidang;
   ```

4. **Test**:
   ```bash
   ./mvnw clean compile
   ```

5. **Commit**:
   ```bash
   git add .
   git commit -m "refactor: rename entity package to model"
   ```

### ✅ Checklist
- [ ] Renamed `entity/` → `model/`
- [ ] Updated package declaration in all model files
- [ ] Updated imports in all other files
- [ ] `./mvnw clean compile` passes
- [ ] Committed changes

---

## Phase 3: Add ApiResponse DTO

**What**: Create standardized response wrapper.  
**Risk**: ⚪ None - just adding new file.

### Step-by-Step

1. **Create file** `src/main/java/com/example/leaflet_geo/dto/ApiResponse.java`:

```java
package com.example.leaflet_geo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standardized API response wrapper.
 * Use this for ALL controller responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    private Long totalCount;
    
    /**
     * Create success response with data
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
            .success(true)
            .message(message)
            .data(data)
            .build();
    }
    
    /**
     * Create success response with data and count
     */
    public static <T> ApiResponse<T> success(String message, T data, Long totalCount) {
        return ApiResponse.<T>builder()
            .success(true)
            .message(message)
            .data(data)
            .totalCount(totalCount)
            .build();
    }
    
    /**
     * Create error response
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
            .success(false)
            .message(message)
            .build();
    }
}
```

2. **Test**:
   ```bash
   ./mvnw clean compile
   ```

3. **Commit**:
   ```bash
   git add .
   git commit -m "feat: add ApiResponse DTO for standardized responses"
   ```

### ✅ Checklist
- [ ] Created `dto/ApiResponse.java`
- [ ] `./mvnw clean compile` passes
- [ ] Committed changes

---

## Phase 4: Refactor Controllers (One at a Time!)

**What**: Replace HashMap responses with ApiResponse.  
**Risk**: 🟠 Medium - test each controller after refactoring.

### ⚠️ WARNING: Do ONE controller at a time, test, then move to next!

### Example Refactor

**BEFORE** (RefKecamatanController.java):
```java
@GetMapping
public ResponseEntity<Map<String, Object>> getAllKecamatan() {
    try {
        List<RefKecamatan> list = refKecamatanRepository.findAll();
        long totalCount = refKecamatanRepository.count();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Data berhasil diambil");
        response.put("totalCount", totalCount);
        response.put("data", list);
        
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Gagal: " + e.getMessage());
        return ResponseEntity.internalServerError().body(response);
    }
}
```

**AFTER**:
```java
@GetMapping
public ResponseEntity<ApiResponse<List<RefKecamatan>>> getAllKecamatan() {
    try {
        List<RefKecamatan> list = refKecamatanRepository.findAll();
        long totalCount = refKecamatanRepository.count();
        return ResponseEntity.ok(
            ApiResponse.success("Data berhasil diambil", list, totalCount)
        );
    } catch (Exception e) {
        return ResponseEntity.internalServerError()
            .body(ApiResponse.error("Gagal: " + e.getMessage()));
    }
}
```

### Controller Refactor Order (do one, test, commit, repeat):

1. [ ] `RefKecamatanController.java` → Test → Commit
2. [ ] `RefKelurahanController.java` → Test → Commit
3. [ ] `BidangController.java` → Test → Commit
4. [ ] `DatObjekPajakController.java` → Test → Commit
5. [ ] `DatSubjekPajakController.java` → Test → Commit
6. [ ] `PendapatanController.java` → Test → Commit
7. [ ] `SimatdaController.java` → Test → Commit
8. [ ] Other controllers...

### After EACH controller:
```bash
./mvnw clean compile
./mvnw spring-boot:run
# Test the endpoints manually
```

---

## Phase 5: Code Quality (Optional)

**What**: Add Javadoc, validation.  
**Risk**: ⚪ None.

### Tasks
- [ ] Add Javadoc to all public classes and methods
- [ ] Add `@Valid` to request body parameters
- [ ] Consider adding `@ControllerAdvice` for global exception handling

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

- [ ] `./mvnw clean compile` passes
- [ ] `./mvnw spring-boot:run` works
- [ ] Test all API endpoints (Postman/curl)
- [ ] Frontend still connects and works
- [ ] No errors in console logs
