// ========================================
// NEWS & EVENTS COMPONENT - TYPESCRIPT
// ========================================

// src/app/components/news-events/news-events.component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subject, takeUntil } from 'rxjs';
import { 
  LucideAngularModule, 
  Calendar, Eye, Clock, MapPin, 
  Users, ArrowRight 
} from 'lucide-angular';
import { NewsService } from '../../services/news.service';
import { EventService } from '../../services/event.service';
import { News } from '../../models/news.model';
import { Event } from '../../models/event.model';

@Component({
  selector: 'app-news-events',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './news-events.component.html',
  styleUrls: ['./news-events.component.css']
})
export class NewsEventsComponent implements OnInit, OnDestroy {
  // Register icons
  readonly Calendar = Calendar;
  readonly Eye = Eye;
  readonly Clock = Clock;
  readonly MapPin = MapPin;
  readonly Users = Users;
  readonly ArrowRight = ArrowRight;

  // Component data
  news: News[] = [];
  events: Event[] = [];
  activeTab: 'all' | 'news' | 'events' = 'all';
  selectedNews: News | null = null;
  selectedEvent: Event | null = null;
  
  private destroy$ = new Subject<void>();

  constructor(
    private newsService: NewsService,
    private eventService: EventService
  ) {}

  ngOnInit(): void {
    // Load news
    this.newsService.getNews()
      .pipe(takeUntil(this.destroy$))
      .subscribe((news: News[]) => {
        this.news = news;
      });

    // Load events
    this.eventService.getEvents()
      .pipe(takeUntil(this.destroy$))
      .subscribe((events: Event[]) => {
        this.events = events;
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  setActiveTab(tab: 'all' | 'news' | 'events'): void {
    this.activeTab = tab;
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

  registerForEvent(eventId: number): void {
    const success = this.eventService.registerForEvent(eventId);
    if (success) {
      alert('Successfully registered for event!');
    } else {
      alert('Event is full or registration failed.');
    }
  }

  getCategoryColor(category: string): string {
    const colors: { [key: string]: string } = {
      'Achievement': 'bg-blue-600',
      'Research': 'bg-purple-600',
      'Student Achievement': 'bg-green-600',
      'Partnership': 'bg-orange-600',
      'Sustainability': 'bg-teal-600'
    };
    return colors[category] || 'bg-gray-600';
  }

  getEventTypeColor(type: string): string {
    const colors: { [key: string]: string } = {
      'Admissions': 'bg-blue-600',
      'Career': 'bg-green-600',
      'Academic': 'bg-purple-600',
      'Student Life': 'bg-orange-600',
      'Business': 'bg-indigo-600',
      'Cultural': 'bg-pink-600'
    };
    return colors[type] || 'bg-gray-600';
  }

  readMore(newsItem: News): void {
    this.selectedNews = newsItem;
    this.selectedEvent = null;
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  closeNewsDetail(): void {
    this.selectedNews = null;
  }

  viewEventDetails(event: Event): void {
    this.selectedEvent = event;
    this.selectedNews = null;
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  closeEventDetail(): void {
    this.selectedEvent = null;
  }

  registerForEventFromDetail(eventId: number): void {
    const success = this.eventService.registerForEvent(eventId);
    if (success) {
      alert('Successfully registered for event!');
      // Refresh event data
      this.eventService.getEvents()
        .pipe(takeUntil(this.destroy$))
        .subscribe((events: Event[]) => {
          this.events = events;
          // Update selected event with new registration count
          if (this.selectedEvent) {
            this.selectedEvent = events.find(e => e.id === eventId) || null;
          }
        });
    } else {
      alert('Event is full or registration failed.');
    }
  }
}