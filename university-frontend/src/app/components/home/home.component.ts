/*
import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
*/

// ========================================
// HOME COMPONENT - TYPESCRIPT
// ========================================

// src/app/components/home/home.component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { 
  LucideAngularModule, 
  Users, BookOpen, Award, Globe, Star, 
  Calendar, Eye, Clock, MapPin, ArrowRight, 
  CheckCircle, Video, ChevronRight, TrendingUp, Target 
} from 'lucide-angular';
import { ProgramService } from '../../services/program.service';
import { NewsService } from '../../services/news.service';
import { EventService } from '../../services/event.service';
import { Program } from '../../models/program.model';
import { News } from '../../models/news.model';
import { Event } from '../../models/event.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, LucideAngularModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {
  // Register all icons
  readonly Users = Users;
  readonly BookOpen = BookOpen;
  readonly Award = Award;
  readonly Globe = Globe;
  readonly Star = Star;
  readonly Calendar = Calendar;
  readonly Eye = Eye;
  readonly Clock = Clock;
  readonly MapPin = MapPin;
  readonly ArrowRight = ArrowRight;
  readonly CheckCircle = CheckCircle;
  readonly Video = Video;
  readonly ChevronRight = ChevronRight;
  readonly TrendingUp = TrendingUp;
  readonly Target = Target;

  // Component data
  featuredPrograms: Program[] = [];
  news: News[] = [];
  events: Event[] = [];
  
  stats = [
    { icon: Users, value: '25,000+', label: 'Active Students', color: 'blue' },
    { icon: BookOpen, value: '200+', label: 'Programs Offered', color: 'green' },
    { icon: Award, value: '95%', label: 'Employment Rate', color: 'purple' },
    { icon: Globe, value: '80+', label: 'Countries Represented', color: 'orange' }
  ];

  whyChooseUs = [
    {
      icon: Award,
      title: 'World-Class Education',
      description: 'Learn from renowned faculty and industry experts with cutting-edge curriculum'
    },
    {
      icon: TrendingUp,
      title: 'Career Success',
      description: '95% employment rate with average starting salary of $75,000'
    },
    {
      icon: Globe,
      title: 'Global Network',
      description: 'Connect with 100,000+ alumni across 150 countries worldwide'
    },
    {
      icon: BookOpen,
      title: 'Research Excellence',
      description: '$150M+ annual research funding with state-of-the-art facilities'
    },
    {
      icon: Users,
      title: 'Diverse Community',
      description: 'Students from 80+ countries creating a rich multicultural environment'
    },
    {
      icon: Target,
      title: 'Innovation Hub',
      description: 'Access to incubators, startup funding, and entrepreneurship programs'
    }
  ];

  private destroy$ = new Subject<void>();

  constructor(
    private programService: ProgramService,
    private newsService: NewsService,
    private eventService: EventService
  ) {}

  ngOnInit(): void {
    // Load featured programs (first 3)
    this.programService.getPrograms()
      .pipe(takeUntil(this.destroy$))
      .subscribe((programs: Program[]) => {
        this.featuredPrograms = programs.slice(0, 3);
      });

    // Load latest news (first 3)
    this.newsService.getNews()
      .pipe(takeUntil(this.destroy$))
      .subscribe((news: News[]) => {
        this.news = news.slice(0, 3);
      });

    // Load upcoming events (first 4)
    this.eventService.getEvents()
      .pipe(takeUntil(this.destroy$))
      .subscribe((events: Event[]) => {
        this.events = events.slice(0, 4);
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', { 
      month: 'short', 
      day: 'numeric', 
      year: 'numeric' 
    });
  }

  getEventDay(dateString: string): number {
    return new Date(dateString).getDate();
  }

  getEventMonth(dateString: string): string {
    return new Date(dateString).toLocaleString('default', { month: 'short' });
  }

  formatNumber(num: number): string {
    return num.toLocaleString();
  }
}
