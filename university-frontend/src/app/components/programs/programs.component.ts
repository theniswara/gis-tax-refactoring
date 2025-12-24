/*
import { Component } from '@angular/core';

@Component({
  selector: 'app-programs',
  standalone: true,
  imports: [],
  templateUrl: './programs.component.html',
  styleUrl: './programs.component.css'
})
export class ProgramsComponent {

}
*/

// src/main// ========================================
// PROGRAMS COMPONENT
// ========================================

// src/app/components/programs/programs.component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { 
  LucideAngularModule, 
  Search, BookOpen, Clock, Users, Star, 
  ArrowRight, CheckCircle, Download, Award 
} from 'lucide-angular';
import { ProgramService } from '../../services/program.service';
import { Program } from '../../models/program.model';

@Component({
  selector: 'app-programs',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, LucideAngularModule],
  templateUrl: './programs.component.html',
  styleUrls: ['./programs.component.css']
})
export class ProgramsComponent implements OnInit, OnDestroy {
  // Icons
  readonly Search = Search;
  readonly BookOpen = BookOpen;
  readonly Clock = Clock;
  readonly Users = Users;
  readonly Star = Star;
  readonly ArrowRight = ArrowRight;
  readonly CheckCircle = CheckCircle;
  readonly Download = Download;
  readonly Award = Award;

  // Data
  allPrograms: Program[] = [];
  filteredPrograms: Program[] = [];
  selectedProgram: Program | null = null;

  // Filters
  searchQuery = '';
  filterDegree = 'all';
  filterField = 'all';

  // Cleanup
  private destroy$ = new Subject<void>();

  constructor(
    private programService: ProgramService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // Load programs
    this.programService.getPrograms()
      .pipe(takeUntil(this.destroy$))
      .subscribe((programs: Program[]) => {
        this.allPrograms = programs;
        this.applyFilters();
      });

    // Check for program ID in route
    this.route.params
      .pipe(takeUntil(this.destroy$))
      .subscribe((params: any) => {
        if (params['id']) {
          const id = parseInt(params['id'], 10);
          this.programService.getProgramById(id)
            .pipe(takeUntil(this.destroy$))
            .subscribe((program: Program) => {
              this.selectedProgram = program;
            });
        }
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  applyFilters(): void {
    this.programService.searchPrograms(
      this.searchQuery,
      this.filterDegree,
      this.filterField
    )
    .pipe(takeUntil(this.destroy$))
    .subscribe((programs: Program[]) => {
      this.filteredPrograms = programs;
    });
  }

  onSearchChange(): void {
    this.applyFilters();
  }

  onDegreeChange(): void {
    this.applyFilters();
  }

  onFieldChange(): void {
    this.applyFilters();
  }

  selectProgram(program: Program): void {
    this.selectedProgram = program;
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  backToList(): void {
    this.selectedProgram = null;
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  downloadBrochure(): void {
    alert('Downloading brochure...');
  }

  contactAdvisor(): void {
    alert('Opening contact form...');
  }

  startApplication(): void {
    alert('Redirecting to application...');
  }
}