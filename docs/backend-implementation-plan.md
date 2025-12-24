# 🎯 Backend Refactoring Guide

> **Who is this for?** The backend developer working on `leaflet-geo`  
> **What will you do?** Clean up the code to match the patterns in `university-backend`

---

## 📖 How to Use This Guide

1. Read each phase completely before starting
2. Do ONE phase at a time
3. Test after each phase
4. If something breaks, use the rollback commands
5. Ask for help if you're stuck!

---

## 🚦 Before You Start (REQUIRED)

### Step 1: Open terminal in the backend folder
```bash
cd "/media/zpreoz/New Volume/College/POLINEMA/PROJECT/PRODUCTION/gis-tax-refactoring/leaflet-geo"
```

### Step 2: Create a new branch (DO NOT skip this!)
```bash
git checkout -b refactor/backend-cleanup
```

**What this does:** Creates a safe copy of the code. If you break something, the original code on `main` branch is still safe.

### Step 3: Verify you're on the new branch
```bash
git branch
```

**You should see:**
```
  main
* refactor/backend-cleanup   <-- The star means you're on this branch
```

---

## ✅ Phase 1: Move Documentation Files

### What are we doing?
Moving markdown (.md) files to a `docs/` folder to keep the root clean.

### Step 1: Create the docs folder
```bash
mkdir -p docs
```

### Step 2: Move the files
```bash
mv DASHBOARD_PENDAPATAN_QUICKSTART.md docs/
mv DATABASE_SETUP.md docs/
mv DAT_OBJEK_PAJAK_API.md docs/
mv MULTIPLE_DATABASE_SETUP.md docs/
mv SIMATDA_TARGET_REALISASI_QUERIES.md docs/
mv WKB_TO_GEOJSON_CONVERSION.md docs/
```

### Step 3: Verify files moved
```bash
ls docs/
```

**You should see:**
```
DASHBOARD_PENDAPATAN_QUICKSTART.md
DATABASE_SETUP.md
DAT_OBJEK_PAJAK_API.md
MULTIPLE_DATABASE_SETUP.md
SIMATDA_TARGET_REALISASI_QUERIES.md
WKB_TO_GEOJSON_CONVERSION.md
```

### Step 4: Save your work
```bash
git add .
git commit -m "docs: move markdown files to docs folder"
```

---

## ✅ Phase 2: Rename entity → model

### What are we doing?
Renaming the folder from `entity/` to `model/` to match the university-backend pattern.

### ⚠️ WARNING: This phase changes many files. Be careful!

---

### Step 1: Rename the folder
```bash
mv src/main/java/com/example/leaflet_geo/entity src/main/java/com/example/leaflet_geo/model
```

### Step 2: Update package declarations in EACH file

Open EACH file in `src/main/java/com/example/leaflet_geo/model/` and change the first line:

**Files to edit:**
1. `Bidang.java`
2. `DatObjekPajak.java`
3. `DatSubjekPajak.java`
4. `KecamatanWithCount.java`
5. `KelurahanWithCount.java`
6. `RefKecamatan.java`
7. `RefKelurahan.java`

**Change this line in EACH file:**
```java
// BEFORE (old)
package com.example.leaflet_geo.entity;

// AFTER (new)
package com.example.leaflet_geo.model;
```

### Step 3: Update imports in all other files

Use Find and Replace in your IDE (Ctrl+Shift+H in VS Code):
- **Find:** `com.example.leaflet_geo.entity`
- **Replace:** `com.example.leaflet_geo.model`
- Click "Replace All"

### Step 4: Test if it compiles
```bash
./mvnw clean compile
```

### Step 5: Did it work?
- ✅ **If BUILD SUCCESS:** Continue to Step 6
- ❌ **If BUILD FAILURE:** Read the error. Usually means you missed updating an import.

### Step 6: Save your work
```bash
git add .
git commit -m "refactor: rename entity package to model"
```

---

## ✅ Phase 3: Create ApiResponse DTO

### What are we doing?
Creating a helper class to make API responses consistent and reduce repeated code.

---

### Step 1: Create the file

Create new file: `src/main/java/com/example/leaflet_geo/dto/ApiResponse.java`

### Step 2: Copy this code into the file

```java
package com.example.leaflet_geo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard API response wrapper.
 * Use this for ALL controller responses to keep them consistent.
 * 
 * Example usage:
 *   return ResponseEntity.ok(ApiResponse.success("Data found", myData));
 *   return ResponseEntity.badRequest().body(ApiResponse.error("Not found"));
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    /**
     * true = success, false = error
     */
    private boolean success;
    
    /**
     * Message describing what happened
     */
    private String message;
    
    /**
     * The actual data (can be any type)
     */
    private T data;
    
    /**
     * Total count (for paginated results)
     */
    private Long totalCount;
    
    /**
     * Create a success response with data
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
            .success(true)
            .message(message)
            .data(data)
            .build();
    }
    
    /**
     * Create a success response with data and total count
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
     * Create an error response
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
            .success(false)
            .message(message)
            .build();
    }
}
```

### Step 3: Test if it compiles
```bash
./mvnw clean compile
```

### Step 4: Save your work
```bash
git add .
git commit -m "feat: add ApiResponse DTO for standardized responses"
```

---

## ✅ Phase 4: Refactor ONE Controller (Example)

### What are we doing?
Replacing the old HashMap pattern with the new ApiResponse class.

### ⚠️ Do ONE controller, test it, then do the next one!

---

### Step 1: Open RefKecamatanController.java

File: `src/main/java/com/example/leaflet_geo/controller/RefKecamatanController.java`

### Step 2: Add the import at the top
```java
import com.example.leaflet_geo.dto.ApiResponse;
```

### Step 3: Change ONE method at a time

**BEFORE (old messy code):**
```java
@GetMapping
public ResponseEntity<Map<String, Object>> getAllKecamatan() {
    try {
        List<RefKecamatan> kecamatanList = refKecamatanRepository.findAll();
        long totalCount = refKecamatanRepository.count();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Data kecamatan berhasil diambil");
        response.put("totalCount", totalCount);
        response.put("data", kecamatanList);
        
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Gagal mengambil data kecamatan: " + e.getMessage());
        response.put("data", null);
        
        return ResponseEntity.internalServerError().body(response);
    }
}
```

**AFTER (new clean code):**
```java
@GetMapping
public ResponseEntity<ApiResponse<List<RefKecamatan>>> getAllKecamatan() {
    try {
        List<RefKecamatan> kecamatanList = refKecamatanRepository.findAll();
        long totalCount = refKecamatanRepository.count();
        
        return ResponseEntity.ok(
            ApiResponse.success("Data kecamatan berhasil diambil", kecamatanList, totalCount)
        );
    } catch (Exception e) {
        return ResponseEntity.internalServerError().body(
            ApiResponse.error("Gagal mengambil data kecamatan: " + e.getMessage())
        );
    }
}
```

### Step 4: Test after EACH method change
```bash
./mvnw clean compile
```

### Step 5: After finishing ONE controller, save
```bash
git add .
git commit -m "refactor: use ApiResponse in RefKecamatanController"
```

### Step 6: Repeat for other controllers
Do the same for:
- [ ] RefKelurahanController.java
- [ ] BidangController.java
- [ ] DatObjekPajakController.java
- [ ] (other controllers...)

---

## ✅ Phase 5: Test Everything

### Step 1: Run the app
```bash
./mvnw spring-boot:run
```

### Step 2: Test the API endpoints
Use Postman or browser to test:

```
GET http://localhost:8080/api/ref-kecamatan
```

**Expected response:**
```json
{
  "success": true,
  "message": "Data kecamatan berhasil diambil",
  "data": [...],
  "totalCount": 10
}
```

---

## ✅ Phase 6: Push Your Changes

### Step 1: Push to GitHub
```bash
git push -u origin refactor/backend-cleanup
```

### Step 2: Create Pull Request
1. Go to: https://github.com/theniswara/gis-tax-refactoring
2. Click "Compare & pull request"
3. Write a title: "Backend: Rename entity to model, add ApiResponse"
4. Click "Create pull request"
5. Wait for review/approval

---

## 🔥 IF SOMETHING BREAKS

### Option 1: Undo all changes (not committed yet)
```bash
git checkout .
```

### Option 2: Go back to last commit
```bash
git reset --hard HEAD
```

### Option 3: Go back to main branch (abandon all changes)
```bash
git checkout main
```

---

## ❓ Common Questions

**Q: What if `./mvnw clean compile` fails?**
A: Read the error message. It usually says which file and line has the problem.

**Q: What if I changed too many files and got confused?**
A: Run `git checkout .` to undo all changes and start over.

**Q: Do I need to do all controllers in one day?**
A: No! Do one controller, commit, take a break. You can continue tomorrow.
