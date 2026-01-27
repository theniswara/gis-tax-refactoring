import { Component, Output, EventEmitter, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MENU } from '../sidebar/menu';
import { MenuItem } from '../sidebar/menu.model';
import { SidebarStateService } from '../../../services/sidebar-state.service';

@Component({
  selector: 'app-thematic-sidebar',
  templateUrl: './thematic-sidebar.component.html',
  styleUrls: ['./thematic-sidebar.component.scss']
})
export class ThematicSidebarComponent implements OnInit {
  @Output() thematicAction = new EventEmitter<string>();
  isTematikExpanded = false;
  tematikMenuItems: MenuItem[] = [];

  // Modal properties
  showCariNopModal = false;
  showKoordinatModal = false;
  nopSearch = '';
  namaSearch = '';
  latitude = '';
  longitude = '';

  constructor(private router: Router, private sidebarStateService: SidebarStateService) {

  }

  ngOnInit() {
    this.loadTematikMenuItems();
  }



  /**
   * Mengambil submenu tematik dari menu.ts berdasarkan id parent 10 (TEMATIK)
   */
  loadTematikMenuItems() {
    const tematikMenu = MENU.find(menu => menu.id === 10);
    if (tematikMenu && tematikMenu.subItems) {
      this.tematikMenuItems = tematikMenu.subItems;
    }
  }

  openCariNopModal() {
    this.showCariNopModal = true;
  }

  openKoordinatModal() {
    this.showKoordinatModal = true;
  }

  closeCariNopModal() {
    this.showCariNopModal = false;
    this.nopSearch = '';
    this.namaSearch = '';
  }

  closeKoordinatModal() {
    this.showKoordinatModal = false;
    this.latitude = '';
    this.longitude = '';
  }

  /**
   * Search NOP
   */
  searchNop() {
    console.log('Searching NOP:', this.nopSearch, this.namaSearch);
    this.thematicAction.emit(`cari-nop-${this.nopSearch}`);
    this.closeCariNopModal();
  }

  /**
   * Set Koordinat
   */
  setKoordinat() {
    console.log('Setting Koordinat:', this.latitude, this.longitude);
    this.thematicAction.emit(`set-koordinat-${this.latitude}-${this.longitude}`);
    this.closeKoordinatModal();
  }

  toggleTematik() {
    this.isTematikExpanded = !this.isTematikExpanded;
  }

  /**
   * Extract tipe tematik dari link (contoh: /tematik/penggunaan-tanah -> penggunaan-tanah)
   */
  getTematikType(link: string | undefined): string {
    if (!link) return '';
    return link.split('/').pop() || '';
  }

  loadTematik(type: string) {
    // Navigate to the specific thematic page
    this.router.navigate(['/tematik', type]);
    this.isTematikExpanded = false;
  }

  /**
   * Kembali ke sidebar utama dan navigate ke dashboard
   */
  goBackToMain() {
    this.sidebarStateService.setSidebarType('main');
    this.router.navigate(['/dashboard-pajak']);
  }
}
