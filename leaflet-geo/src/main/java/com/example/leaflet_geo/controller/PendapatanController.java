package com.example.leaflet_geo.controller;

import com.example.leaflet_geo.dto.ApiResponse;
import com.example.leaflet_geo.dto.DashboardSummaryDTO;
import com.example.leaflet_geo.dto.TargetRealisasiDTO;
import com.example.leaflet_geo.dto.TopKontributorDTO;
import com.example.leaflet_geo.dto.TrendBulananDTO;
import com.example.leaflet_geo.service.PendapatanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pendapatan")
@CrossOrigin(origins = "*")
public class PendapatanController {

    private final PendapatanService pendapatanService;

    public PendapatanController(PendapatanService pendapatanService) {
        this.pendapatanService = pendapatanService;
    }

    /**
     * Get Dashboard Summary
     */
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<DashboardSummaryDTO>> getDashboardSummary(
            @RequestParam(defaultValue = "2025") Integer tahun) {
        try {
            DashboardSummaryDTO summary = pendapatanService.getDashboardSummary(tahun);
            return ResponseEntity.ok(
                    ApiResponse.success("Dashboard summary untuk tahun " + tahun + " berhasil diambil", summary));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ApiResponse.error("Gagal mengambil dashboard summary: " + e.getMessage()));
        }
    }

    /**
     * Get Target vs Realisasi per Jenis Pajak
     */
    @GetMapping("/target-realisasi")
    public ResponseEntity<ApiResponse<List<TargetRealisasiDTO>>> getTargetRealisasi(
            @RequestParam(defaultValue = "2025") Integer tahun) {
        try {
            List<TargetRealisasiDTO> data = pendapatanService.getTargetRealisasiPerJenis(tahun);
            return ResponseEntity.ok(
                    ApiResponse.success("Target realisasi tahun " + tahun + " berhasil diambil", data,
                            (long) data.size()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ApiResponse.error("Gagal mengambil target realisasi: " + e.getMessage()));
        }
    }

    /**
     * Get Trend Bulanan (Kumulatif)
     */
    @GetMapping("/trend-bulanan")
    public ResponseEntity<ApiResponse<List<TrendBulananDTO>>> getTrendBulanan(
            @RequestParam(defaultValue = "2025") Integer tahun) {
        try {
            List<TrendBulananDTO> data = pendapatanService.getTrendBulanan(tahun);
            return ResponseEntity.ok(
                    ApiResponse.success("Trend bulanan tahun " + tahun + " berhasil diambil", data,
                            (long) data.size()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ApiResponse.error("Gagal mengambil trend bulanan: " + e.getMessage()));
        }
    }

    /**
     * Get Top Kontributor
     */
    @GetMapping("/top-kontributor")
    public ResponseEntity<ApiResponse<List<TopKontributorDTO>>> getTopKontributor(
            @RequestParam(defaultValue = "2025") Integer tahun,
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<TopKontributorDTO> data = pendapatanService.getTopKontributor(tahun, limit);
            return ResponseEntity.ok(
                    ApiResponse.success("Top " + limit + " kontributor tahun " + tahun + " berhasil diambil", data,
                            (long) data.size()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ApiResponse.error("Gagal mengambil top kontributor: " + e.getMessage()));
        }
    }

    /**
     * Get Realisasi by Jenis Pajak (Detail)
     */
    @GetMapping("/realisasi-by-jenis")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getRealisasiByJenis(
            @RequestParam(defaultValue = "2025") Integer tahun,
            @RequestParam(required = false) String jenisPajakId) {
        try {
            List<Map<String, Object>> data = pendapatanService.getRealisasiByJenisPajak(tahun, jenisPajakId);
            return ResponseEntity.ok(
                    ApiResponse.success("Realisasi by jenis tahun " + tahun + " berhasil diambil", data,
                            (long) data.size()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ApiResponse.error("Gagal mengambil realisasi by jenis: " + e.getMessage()));
        }
    }
}
