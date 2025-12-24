/*
import { Component } from '@angular/core';

@Component({
  selector: 'app-admissions',
  standalone: true,
  imports: [],
  templateUrl: './admissions.component.html',
  styleUrl: './admissions.component.css'
})
export class AdmissionsComponent {

}
*/

// ========================================
// ADMISSIONS COMPONENT - TYPESCRIPT
// ========================================

// src/app/components/admissions/admissions.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { 
  LucideAngularModule, 
  Bell, Calendar, CheckCircle, Users, 
  FileText, Mail, Phone, Video, Award, 
  ChevronRight 
} from 'lucide-angular';

@Component({
  selector: 'app-admissions',
  standalone: true,
  imports: [CommonModule, RouterModule, LucideAngularModule],
  templateUrl: './admissions.component.html',
  styleUrls: ['./admissions.component.css']
})
export class AdmissionsComponent {
  // Register icons
  readonly Bell = Bell;
  readonly Calendar = Calendar;
  readonly CheckCircle = CheckCircle;
  readonly Users = Users;
  readonly FileText = FileText;
  readonly Mail = Mail;
  readonly Phone = Phone;
  readonly Video = Video;
  readonly Award = Award;
  readonly ChevronRight = ChevronRight;

  // Application process steps
  applicationSteps = [
    { step: '1', title: 'Create Account', desc: 'Register on our application portal', icon: Users },
    { step: '2', title: 'Complete Form', desc: 'Fill out application details', icon: FileText },
    { step: '3', title: 'Submit Documents', desc: 'Upload required materials', icon: CheckCircle },
    { step: '4', title: 'Track Status', desc: 'Monitor application progress', icon: CheckCircle }
  ];

  // Important dates
  importantDates = [
    { title: 'Application Opens', date: 'November 1, 2025', status: 'open' },
    { title: 'Early Decision Deadline', date: 'December 15, 2025', status: 'upcoming' },
    { title: 'Regular Decision Deadline', date: 'February 1, 2026', status: 'upcoming' },
    { title: 'Financial Aid Deadline', date: 'February 15, 2026', status: 'upcoming' },
    { title: 'Admission Decisions Released', date: 'March 30, 2026', status: 'future' },
    { title: 'Enrollment Deposit Due', date: 'May 1, 2026', status: 'future' }
  ];

  // Application requirements
  requirements = [
    'Completed online application form',
    'Official high school transcripts',
    'Standardized test scores (SAT/ACT)',
    'Two letters of recommendation',
    'Personal statement essay (500-650 words)',
    'Application fee ($75, waiver available)',
    'Resume or activity list',
    'English proficiency test (TOEFL/IELTS for international students)'
  ];

  // Admission types
  admissionTypes = [
    {
      title: 'Early Decision',
      deadline: 'Dec 15, 2025',
      description: 'Binding agreement for students who have Excellence as their first choice',
      benefits: ['Earlier decision', 'Higher acceptance rate', 'Priority housing']
    },
    {
      title: 'Regular Decision',
      deadline: 'Feb 1, 2026',
      description: 'Standard application timeline with full consideration',
      benefits: ['More time to prepare', 'Compare offers', 'Financial aid options']
    },
    {
      title: 'Transfer Students',
      deadline: 'Mar 15, 2026',
      description: 'For students transferring from other institutions',
      benefits: ['Credit evaluation', 'Advanced standing', 'Transfer scholarships']
    }
  ];

  // Financial aid benefits
  financialAidBenefits = [
    'Merit-based scholarships',
    'Need-based financial aid',
    'Athletic scholarships',
    'International student scholarships',
    'Work-study programs',
    'Graduate assistantships'
  ];

  getStatusClass(status: string): string {
    if (status === 'open') return 'bg-green-100 text-green-700';
    if (status === 'upcoming') return 'bg-yellow-100 text-yellow-700';
    return 'bg-gray-100 text-gray-600';
  }

  getStatusText(status: string): string {
    if (status === 'open') return 'Open';
    if (status === 'upcoming') return 'Upcoming';
    return 'Future';
  }

  showNotification(message: string): void {
    alert(message);
  }
}