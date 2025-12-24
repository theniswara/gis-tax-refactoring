/*
import { Component } from '@angular/core';

@Component({
  selector: 'app-research',
  standalone: true,
  imports: [],
  templateUrl: './research.component.html',
  styleUrl: './research.component.css'
})
export class ResearchComponent {

}
*/

// ========================================
// RESEARCH COMPONENT - TYPESCRIPT
// ========================================

// src/app/components/research/research.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { 
  LucideAngularModule, 
  TrendingUp, FileText, Award, Users 
} from 'lucide-angular';

@Component({
  selector: 'app-research',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './research.component.html',
  styleUrls: ['./research.component.css']
})
export class ResearchComponent {
  // Register icons
  readonly TrendingUp = TrendingUp;
  readonly FileText = FileText;
  readonly Award = Award;
  readonly Users = Users;

  // Research impact stats
  stats = [
    { value: '$150M+', label: 'Annual Research Funding', icon: TrendingUp, color: 'blue' },
    { value: '500+', label: 'Publications per Year', icon: FileText, color: 'green' },
    { value: '75+', label: 'Patent Applications', icon: Award, color: 'purple' },
    { value: '200+', label: 'Industry Partnerships', icon: Users, color: 'orange' }
  ];

  // Research centers data
  researchCenters = [
    { 
      name: 'AI & Machine Learning Lab', 
      focus: 'Artificial Intelligence, Deep Learning, Neural Networks', 
      projects: 45, 
      funding: '$12M' 
    },
    { 
      name: 'Biomedical Research Center', 
      focus: 'Cancer Research, Genetics, Immunology', 
      projects: 62, 
      funding: '$25M' 
    },
    { 
      name: 'Sustainable Energy Institute', 
      focus: 'Renewable Energy, Climate Science, Green Tech', 
      projects: 38, 
      funding: '$18M' 
    },
    { 
      name: 'Quantum Computing Lab', 
      focus: 'Quantum Algorithms, Hardware, Cryptography', 
      projects: 28, 
      funding: '$15M' 
    },
    { 
      name: 'Neuroscience Center', 
      focus: 'Brain Research, Cognition, Mental Health', 
      projects: 41, 
      funding: '$20M' 
    },
    { 
      name: 'Space Exploration Lab', 
      focus: 'Astrophysics, Satellite Technology, Cosmology', 
      projects: 33, 
      funding: '$22M' 
    }
  ];

  // Featured research projects
  featuredProjects = [
    { 
      title: 'Breakthrough in Cancer Immunotherapy', 
      category: 'Medicine', 
      impact: 'High',
      description: 'Revolutionary approach to treating aggressive cancers using advanced immunotherapy techniques'
    },
    { 
      title: 'Next-Gen Sustainable Battery Technology', 
      category: 'Engineering', 
      impact: 'High',
      description: 'Development of eco-friendly batteries with 3x longer lifespan and faster charging'
    },
    { 
      title: 'AI Model for Early Disease Detection', 
      category: 'Computer Science', 
      impact: 'Medium',
      description: 'Machine learning system that predicts disease onset up to 5 years in advance'
    }
  ];

  getImpactClass(impact: string): string {
    return impact === 'High' ? 'bg-green-500' : 'bg-yellow-500';
  }
}
