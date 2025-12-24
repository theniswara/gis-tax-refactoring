/*
import { Component } from '@angular/core';

@Component({
  selector: 'app-about',
  standalone: true,
  imports: [],
  templateUrl: './about.component.html',
  styleUrl: './about.component.css'
})
export class AboutComponent {

}
*/

// ========================================
// ABOUT COMPONENT - TYPESCRIPT
// ========================================

// src/app/components/about/about.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { 
  LucideAngularModule, 
  Target, Eye, MapPin, Globe, BookOpen 
} from 'lucide-angular';

@Component({
  selector: 'app-about',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css']
})
export class AboutComponent {
  // Register icons
  readonly Target = Target;
  readonly Eye = Eye;
  readonly MapPin = MapPin;
  readonly Globe = Globe;
  readonly BookOpen = BookOpen;

  // University history timeline
  milestones = [
    { 
      year: '1950', 
      event: 'University Founded', 
      desc: 'Started as a small college with 200 students and 15 faculty members' 
    },
    { 
      year: '1975', 
      event: 'Research Excellence', 
      desc: 'Established first research center, marking our commitment to innovation' 
    },
    { 
      year: '1990', 
      event: 'Global Expansion', 
      desc: 'Opened international programs and partnerships with universities worldwide' 
    },
    { 
      year: '2005', 
      event: 'Digital Transformation', 
      desc: 'Launched online learning platform and modernized all facilities' 
    },
    { 
      year: '2020', 
      event: 'Innovation Hub', 
      desc: 'Created startup incubator supporting over 100 student entrepreneurs' 
    },
    { 
      year: '2025', 
      event: 'Top 100 Global Ranking', 
      desc: 'Achieved recognition as a world-class research university' 
    }
  ];

  // University leadership
  leadership = [
    { name: 'Dr. Robert Williams', title: 'President', image: 'RW' },
    { name: 'Dr. Sarah Mitchell', title: 'Provost', image: 'SM' },
    { name: 'Prof. James Chen', title: 'Dean of Research', image: 'JC' },
    { name: 'Dr. Lisa Anderson', title: 'Dean of Students', image: 'LA' }
  ];

  // Campus locations
  campuses = [
    { 
      name: 'Main Campus', 
      location: '123 University Avenue, Metro City', 
      area: '150 acres', 
      buildings: '45 buildings', 
      image: 'https://images.unsplash.com/photo-1562774053-701939374585?w=400' 
    },
    { 
      name: 'Medical Campus', 
      location: '456 Health Boulevard, Metro City', 
      area: '50 acres', 
      buildings: '12 buildings', 
      image: 'https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?w=400' 
    },
    { 
      name: 'Research Park', 
      location: '789 Innovation Drive, Tech Valley', 
      area: '200 acres', 
      buildings: '20 buildings', 
      image: 'https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?w=400' 
    }
  ];

  // Core values
  coreValues = [
    {
      title: 'Excellence',
      description: 'We pursue the highest standards in teaching, research, and service',
      icon: Target
    },
    {
      title: 'Innovation',
      description: 'We embrace creativity and pioneering approaches to education',
      icon: Eye
    },
    {
      title: 'Integrity',
      description: 'We uphold honesty, ethics, and accountability in all we do',
      icon: BookOpen
    },
    {
      title: 'Diversity',
      description: 'We celebrate and learn from our diverse community',
      icon: Globe
    }
  ];

  // Accreditations
  accreditations = [
    'Higher Learning Commission (HLC)',
    'Association of American Universities (AAU)',
    'AACSB International (Business Programs)',
    'ABET (Engineering Programs)',
    'LCME (Medical School)',
    'ABA (Law School)'
  ];
}