# 📚 Understanding Dashboard Pajak: A Beginner's Learning Guide

> **What You'll Learn:** How to make a webpage display data from a database - the complete journey from database → backend → frontend.

---

## 🎯 Table of Contents

1. [The Big Picture - What Did We Build?](#chapter-1-the-big-picture)
2. [The 3 Layers - Understanding the Architecture](#chapter-2-the-3-layers)
3. [Layer 1: Backend DTO - The Data Container](#chapter-3-the-dto)
4. [Layer 2: Backend Service - The Data Fetcher](#chapter-4-the-service)
5. [Layer 3: Backend Controller - The API Door](#chapter-5-the-controller)
6. [Layer 4: Frontend Service - The API Caller](#chapter-6-frontend-service)
7. [Layer 5: Frontend Component - The Display](#chapter-7-frontend-component)
8. [How It All Connects - The Complete Flow](#chapter-8-the-complete-flow)
9. [Practice Exercise: Build Your Own Simple Version](#chapter-9-practice)

---

# Chapter 1: The Big Picture

## What Did We Build?

Before, your Dashboard Pajak loaded data from a **static JSON file** - a file that never changes:

```
📄 master-pajak.json (static file - just sitting there, never updates)
   ↓
🖥️ Dashboard Pajak (shows the data)
```

After our changes, it now loads data from a **real database** through an **API**:

```
🗄️ Database (real data that can change)
   ↓
☕ Spring Boot Backend (fetches from database)
   ↓
🔌 API Endpoint (the "door" the frontend knocks on)
   ↓
🅰️ Angular Frontend (calls the API)
   ↓
🖥️ Dashboard Pajak (shows the data)
```

## Why Is This Better?

| Static JSON | Dynamic API |
|-------------|-------------|
| Data never changes | Data is always fresh |
| Hardcoded values | Real values from database |
| Need to edit file to update | Updates automatically |

---

# Chapter 2: The 3 Layers

## Think of it like a Restaurant 🍽️

Imagine you're at a restaurant:

```
👤 Customer (YOU at the dashboard) 
        ↓ "I want to see tax data for 2025"
        
🧑‍🍳 Waiter (Angular Frontend)
        ↓ Takes your order to the kitchen
        
👨‍🍳 Kitchen (Spring Boot Backend)
        ↓ Gets ingredients and cooks
        
🧊 Refrigerator (Database)
        Has all the raw ingredients (data)
```

**In code terms:**

| Restaurant | Code |
|------------|------|
| Customer's table | `dashboard-pajak.component.html` (what you see) |
| Waiter | `pajak.service.ts` (fetches data for you) |
| Kitchen door | `PendapatanController.java` (API endpoint) |
| Cook | `PendapatanService.java` (processes data) |
| Refrigerator | MySQL/PostgreSQL/Oracle databases |
| Plate to serve | `PajakDataDTO.java` (data container) |

---

# Chapter 3: The DTO (Data Transfer Object)

## What is a DTO?

A DTO is like a **shipping box** 📦. When you send something from one place to another, you put it in a box with a specific shape.

## The Code We Created:

```java
// File: PajakDataDTO.java
// Think of this as defining the SHAPE of our shipping box

@Data                    // ← This is called an "annotation" - it auto-creates getters/setters
@NoArgsConstructor       // ← Creates empty constructor: new PajakDataDTO()
@AllArgsConstructor      // ← Creates full constructor: new PajakDataDTO("Perhotelan", 2025, "Januari", 1000000)
public class PajakDataDTO {
    
    private String kategori;      // "Perhotelan", "Restoran", etc.
    private Integer tahun;        // 2025
    private String bulan;         // "Januari", "Februari", etc.
    private BigDecimal value;     // The money amount: 1,344,843,493
    
}
```

### Breaking It Down:

**1. `@Data`** - This is Lombok magic! 🪄
```java
// Without @Data, you would need to write ALL of this:
public String getKategori() { return kategori; }
public void setKategori(String kategori) { this.kategori = kategori; }
public Integer getTahun() { return tahun; }
public void setTahun(Integer tahun) { this.tahun = tahun; }
// ... and more for each field!

// With @Data, you write NOTHING. It's automatic!
```

**2. `private String kategori;`** - This declares a field:
- `private` = Only this class can access it directly
- `String` = It's text (like "Perhotelan")
- `kategori` = The name we gave it

### Real Example:

When the database says "Pajak Hotel earned 1,344,843,493 in January 2025", we put it in our DTO box:

```java
PajakDataDTO data = new PajakDataDTO();
data.setKategori("Perhotelan");          // Set the category
data.setTahun(2025);                      // Set the year
data.setBulan("Januari");                 // Set the month
data.setValue(new BigDecimal(1344843493)); // Set the amount
```

---

# Chapter 4: The Service (Data Fetcher)

## What is a Service?

The Service is like the **cook in the kitchen** 👨‍🍳. It:
- Knows HOW to get data from the database
- Processes/transforms the data
- Puts it in DTO boxes ready to serve

## The Code We Created (Simplified):

```java
// File: PendapatanService.java

@Service  // ← Tells Spring: "This is a service class!"
public class PendapatanService {

    private final JdbcTemplate mysqlJdbcTemplate;  // ← Tool to talk to MySQL database
    
    // The method we created:
    public List<PajakDataDTO> getRealisasiBulananByKategori(Integer tahun) {
        
        // Step 1: Write SQL to ask the database for data
        String sql = """
            SELECT
                j.s_namajenis AS jenis_pajak,
                MONTH(t.t_tglpembayaran) AS bulan,
                SUM(t.t_jmlhpembayaran) AS total_realisasi
            FROM t_transaksi t
            JOIN s_jenisobjek j ON t.t_jenispajak = j.s_idjenis
            WHERE YEAR(t.t_tglpembayaran) = ?
            GROUP BY j.s_namajenis, MONTH(t.t_tglpembayaran)
            """;
        
        // Step 2: Run the SQL and convert each row to a DTO
        List<PajakDataDTO> results = mysqlJdbcTemplate.query(sql, 
            (rs, rowNum) -> {
                PajakDataDTO dto = new PajakDataDTO();
                dto.setKategori(rs.getString("jenis_pajak"));
                dto.setTahun(tahun);
                dto.setBulan(getNamaBulan(rs.getInt("bulan")));
                dto.setValue(rs.getBigDecimal("total_realisasi"));
                return dto;
            }, 
            tahun  // ← The ? in SQL gets replaced with this
        );
        
        return results;  // Return the list of DTOs
    }
}
```

### Breaking It Down:

**1. `@Service`** - Annotation that tells Spring Boot:
> "Hey Spring! This class is a service. Please manage it for me!"

**2. `JdbcTemplate`** - A tool to talk to databases:
```java
// Think of it like a translator that speaks "Database language"
JdbcTemplate = "Hey database, give me data!"
Database replies → JdbcTemplate translates → Java objects
```

**3. `List<PajakDataDTO>`** - A list (array) of DTO objects:
```java
// Like having multiple shipping boxes:
[
    PajakDataDTO{ kategori: "Perhotelan", bulan: "Januari", value: 100 },
    PajakDataDTO{ kategori: "Perhotelan", bulan: "Februari", value: 200 },
    PajakDataDTO{ kategori: "Restoran", bulan: "Januari", value: 300 },
    // ... more boxes
]
```

**4. The SQL Query:**
```sql
SELECT
    j.s_namajenis AS jenis_pajak,    -- Get the tax type name
    MONTH(t.t_tglpembayaran) AS bulan, -- Get the month (1, 2, 3...)
    SUM(t.t_jmlhpembayaran) AS total  -- Sum up all payments
FROM t_transaksi t                     -- From transactions table
JOIN s_jenisobjek j ON ...             -- Join with tax types table
WHERE YEAR(t.t_tglpembayaran) = ?      -- Filter by year (? = parameter)
GROUP BY ...                           -- Group by tax type and month
```

**5. The Lambda `(rs, rowNum) -> { ... }`:**
```java
// This runs for EACH ROW returned by the database
// rs = ResultSet (the row data)
// rowNum = Which row number (0, 1, 2...)

// For each row, create a new DTO and fill it:
(rs, rowNum) -> {
    PajakDataDTO dto = new PajakDataDTO();
    dto.setKategori(rs.getString("jenis_pajak"));  // Get "jenis_pajak" column
    return dto;
}
```

---

# Chapter 5: The Controller (API Door)

## What is a Controller?

The Controller is like the **reception desk** 🚪. It:
- Receives requests from the outside world
- Knows which service to call
- Returns the response

## The Code We Created:

```java
// File: PendapatanController.java

@RestController                         // ← This is a REST API controller
@RequestMapping("/api/pendapatan")     // ← Base URL path
public class PendapatanController {

    private final PendapatanService pendapatanService;
    
    // Constructor - Spring automatically injects the service
    public PendapatanController(PendapatanService pendapatanService) {
        this.pendapatanService = pendapatanService;
    }
    
    // The endpoint we created:
    @GetMapping("/pajak-bulanan")      // ← URL: /api/pendapatan/pajak-bulanan
    public ResponseEntity<ApiResponse<List<PajakDataDTO>>> getPajakBulanan(
            @RequestParam(defaultValue = "2025") Integer tahun) {
        
        try {
            // Call the service to get data
            List<PajakDataDTO> data = pendapatanService.getRealisasiBulananByKategori(tahun);
            
            // Wrap it in a nice response
            return ResponseEntity.ok(
                ApiResponse.success("Data berhasil diambil", data)
            );
        } catch (Exception e) {
            // If something goes wrong, return an error
            return ResponseEntity.internalServerError()
                .body(ApiResponse.error("Gagal: " + e.getMessage()));
        }
    }
}
```

### Breaking It Down:

**1. `@RestController`** - Tells Spring:
> "This class handles web requests and returns JSON responses"

**2. `@RequestMapping("/api/pendapatan")`** - Sets the base URL:
```
http://localhost:8080/api/pendapatan/...
                     └───────────────┘
                     This part comes from @RequestMapping
```

**3. `@GetMapping("/pajak-bulanan")`** - This method handles GET requests:
```
GET http://localhost:8080/api/pendapatan/pajak-bulanan
                                        └─────────────┘
                                        This part comes from @GetMapping
```

**4. `@RequestParam`** - Gets parameters from the URL:
```
http://localhost:8080/api/pendapatan/pajak-bulanan?tahun=2025
                                                   └────────┘
                                                   @RequestParam gets this!
```

**5. `ResponseEntity<...>`** - The response wrapper:
```java
// Returns HTTP 200 OK with JSON body:
ResponseEntity.ok(data)

// Returns HTTP 500 Error:
ResponseEntity.internalServerError().body(error)
```

---

# Chapter 6: Frontend Service (API Caller)

## What is an Angular Service?

An Angular Service is like a **helper class** that handles specific tasks. Our `PajakService` handles talking to the backend API.

## The Code We Created:

```typescript
// File: pajak.service.ts

@Injectable({
  providedIn: 'root'  // ← Available everywhere in the app
})
export class PajakService {
  
  private apiUrl = `${environment.apiUrl}api/pendapatan`;
  
  constructor(private http: HttpClient) { }  // ← Tool to make HTTP requests
  
  getPajakBulanan(tahun: number = 2025): Observable<PajakData[]> {
    
    const params = new HttpParams().set('tahun', tahun.toString());
    
    return this.http.get<ApiResponse<PajakData[]>>(`${this.apiUrl}/pajak-bulanan`, { params })
      .pipe(map(response => response.data || []));
  }
}
```

### Breaking It Down:

**1. `@Injectable({ providedIn: 'root' })`:**
> "Angular, make this service available everywhere. Create only ONE instance."

**2. `HttpClient`** - Angular's tool for HTTP requests:
```typescript
// Like a messenger that goes to the backend and comes back with data
this.http.get(url)  // Send GET request
this.http.post(url) // Send POST request
```

**3. `Observable<PajakData[]>`:**
```typescript
// An Observable is like a "promise of future data"
// It's saying: "I will give you an array of PajakData... eventually"

// Think of it like ordering pizza:
// Observable = The order (promise of pizza)
// subscribe() = When pizza arrives, eat it!
```

**4. `.pipe(map(response => response.data))`:**
```typescript
// The API returns: { success: true, message: "...", data: [...] }
// But we only want the "data" part
// So we use "pipe" and "map" to extract it:

// Before pipe: { success: true, data: [item1, item2] }
// After pipe:  [item1, item2]
```

---

# Chapter 7: Frontend Component (The Display)

## What is a Component?

A Component is a **piece of your webpage**. It has:
- **Template (HTML)** - What you see
- **Styles (CSS)** - How it looks
- **Logic (TypeScript)** - What it does

## The Code We Modified:

```typescript
// File: dashboard-pajak.component.ts

export class DashboardPajakComponent implements OnInit {
  
  pajakData: PajakData[] = [];  // Store the data here
  isLoading: boolean = false;    // Are we loading?
  errorMessage: string = '';     // Any errors?
  
  constructor(private pajakService: PajakService) { }  // Inject the service
  
  ngOnInit(): void {
    this.loadPajakData();  // Load data when component starts
  }
  
  loadPajakData(): void {
    this.isLoading = true;      // Show loading spinner
    this.errorMessage = '';     // Clear any old errors
    
    // Call the service to get data
    this.pajakService.getPajakBulanan(this.selectedYear).subscribe({
      
      // SUCCESS: Data arrived!
      next: (data: PajakData[]) => {
        this.pajakData = data;  // Save the data
        this.processData();     // Process it (create charts)
        this.isLoading = false; // Hide loading spinner
      },
      
      // ERROR: Something went wrong!
      error: (error) => {
        console.error('Error:', error);
        this.errorMessage = 'Gagal memuat data...';
        this.isLoading = false;
        this.loadFallbackData();  // Try loading static JSON instead
      }
    });
  }
}
```

### Breaking It Down:

**1. `implements OnInit`:**
```typescript
// OnInit is a "lifecycle hook" - code that runs at specific times
// ngOnInit() runs when the component is first created
// Perfect place to load data!
```

**2. `constructor(private pajakService: PajakService)`:**
```typescript
// This is "Dependency Injection"
// Angular automatically creates a PajakService and gives it to us
// We don't need to do: new PajakService() - Angular handles it!
```

**3. `subscribe({ next, error })`:**
```typescript
// Remember Observable is like ordering pizza?
// subscribe() is when you wait for it and handle when it arrives

pajakService.getPajakBulanan().subscribe({
  next: (data) => {
    // 🎉 Pizza arrived! (Data loaded successfully)
    console.log("Got data:", data);
  },
  error: (error) => {
    // 😢 Pizza never came... (Error happened)
    console.log("Error:", error);
  }
});
```

## The Template (HTML):

```html
<!-- Show loading spinner when isLoading is true -->
<div *ngIf="isLoading">
  <div class="spinner-border">Loading...</div>
</div>

<!-- Show content when NOT loading -->
<div *ngIf="!isLoading">
  <div *ngFor="let kategori of kategoris">
    {{ kategori }} - {{ getTotalByCategory(kategori) }}
  </div>
</div>

<!-- Show error if there is one -->
<div *ngIf="errorMessage" class="alert">
  {{ errorMessage }}
</div>
```

### Breaking It Down:

**1. `*ngIf="isLoading"`:**
```html
<!-- Only show this div IF isLoading is true -->
<div *ngIf="isLoading">I am visible when loading!</div>
```

**2. `*ngFor="let kategori of kategoris"`:**
```html
<!-- Loop through each item in kategoris array -->
<!-- Like a for-each loop in programming -->
<div *ngFor="let kategori of kategoris">
  {{ kategori }}
</div>

<!-- If kategoris = ["Perhotelan", "Restoran", "Parkir"], this creates: -->
<div>Perhotelan</div>
<div>Restoran</div>
<div>Parkir</div>
```

**3. `{{ expression }}`:**
```html
<!-- Double curly braces = show this value -->
<p>Year: {{ selectedYear }}</p>    <!-- Shows: Year: 2025 -->
<p>Name: {{ kategori }}</p>        <!-- Shows: Name: Perhotelan -->
```

---

# Chapter 8: The Complete Flow

## Let's Follow a Request from Start to Finish:

```
1️⃣ USER opens Dashboard Pajak in browser
   ↓
2️⃣ ANGULAR loads DashboardPajakComponent
   ↓
3️⃣ ngOnInit() calls loadPajakData()
   ↓
4️⃣ loadPajakData() sets isLoading = true (spinner shows)
   ↓
5️⃣ pajakService.getPajakBulanan(2025) is called
   ↓
6️⃣ HttpClient sends: GET http://localhost:8080/api/pendapatan/pajak-bulanan?tahun=2025
   ↓
7️⃣ SPRING BOOT receives request at PendapatanController
   ↓
8️⃣ Controller calls: pendapatanService.getRealisasiBulananByKategori(2025)
   ↓
9️⃣ Service runs SQL query against MySQL database
   ↓
🔟 Database returns rows of data
   ↓
1️⃣1️⃣ Service converts rows to List<PajakDataDTO>
   ↓
1️⃣2️⃣ Controller wraps in ApiResponse and returns
   ↓
1️⃣3️⃣ Response travels back over HTTP as JSON
   ↓
1️⃣4️⃣ HttpClient receives JSON, Angular parses it
   ↓
1️⃣5️⃣ .pipe(map()) extracts the data array
   ↓
1️⃣6️⃣ subscribe() callback receives the data
   ↓
1️⃣7️⃣ Component saves data and sets isLoading = false
   ↓
1️⃣8️⃣ Angular re-renders the template with new data
   ↓
1️⃣9️⃣ USER sees the charts! 🎉
```

## The JSON Data at Each Step:

**Database returns:**
```
| jenis_pajak  | bulan | total_realisasi |
|--------------|-------|-----------------|
| Pajak Hotel  | 1     | 100000000       |
| Pajak Hotel  | 2     | 120000000       |
```

**Service converts to DTOs:**
```java
[
  PajakDataDTO{ kategori: "Perhotelan", tahun: 2025, bulan: "Januari", value: 100000000 },
  PajakDataDTO{ kategori: "Perhotelan", tahun: 2025, bulan: "Februari", value: 120000000 }
]
```

**Controller returns JSON:**
```json
{
  "success": true,
  "message": "Data berhasil diambil",
  "data": [
    { "kategori": "Perhotelan", "tahun": 2025, "bulan": "Januari", "value": 100000000 },
    { "kategori": "Perhotelan", "tahun": 2025, "bulan": "Februari", "value": 120000000 }
  ]
}
```

**Frontend extracts and displays:**
```
📊 Perhotelan - Rp 220,000,000
```

---

# Chapter 9: Practice Exercises

## Exercise 1: Trace the Code 🔍

Open these files and match each part:

| Step | File | Look For |
|------|------|----------|
| 1 | `PajakDataDTO.java` | The 4 fields (kategori, tahun, bulan, value) |
| 2 | `PendapatanService.java` | `getRealisasiBulananByKategori` method |
| 3 | `PendapatanController.java` | `@GetMapping("/pajak-bulanan")` |
| 4 | `pajak.service.ts` | `getPajakBulanan` method |
| 5 | `dashboard-pajak.component.ts` | `loadPajakData` method |

## Exercise 2: Test the API 🧪

Open your terminal and run:
```bash
curl "http://localhost:8080/api/pendapatan/pajak-bulanan?tahun=2025"
```

Can you identify the kategori names in the response?

## Exercise 3: Break It and Fix It 🔧

1. In `pajak.service.ts`, change the URL to wrong one
2. Save and see what happens in the dashboard
3. Check the browser console (F12 → Console)
4. Fix it back!

## Exercise 4: Add a Console Log 📝

In `loadPajakData()`, add this line:
```typescript
this.pajakService.getPajakBulanan(this.selectedYear).subscribe({
  next: (data: PajakData[]) => {
    console.log('Data received:', data);  // ← Add this!
    console.log('Number of items:', data.length);  // ← And this!
    // ... rest of code
```

Open browser console (F12) and refresh the page. What do you see?

---

# 🎓 Key Takeaways

1. **Data flows in one direction:** Database → Backend → API → Frontend → Display

2. **Each layer has a specific job:**
   - DTO = Data container
   - Service = Business logic
   - Controller = API endpoints  
   - Frontend Service = HTTP calls
   - Component = Display and user interaction

3. **Annotations are shortcuts:**
   - `@Data` = Auto-generate getters/setters
   - `@Service` = Tell Spring "this is a service"
   - `@GetMapping` = Handle GET requests
   - `@Injectable` = Tell Angular "this is a service"

4. **Observable is a promise of future data:**
   - Call `subscribe()` to get the data when it arrives
   - Handle both `next` (success) and `error` (failure)

---

# 📚 What's Next?

This guide covered one feature. If you want, I can create more chapters covering:
- [ ] How authentication works
- [ ] How the map features work
- [ ] Database connections and configuration
- [ ] Error handling patterns
- [ ] How to create new features from scratch

Just let me know what you'd like to learn next! 🚀
