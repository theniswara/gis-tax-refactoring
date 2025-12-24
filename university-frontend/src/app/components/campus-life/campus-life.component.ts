// ========================================
// CAMPUS LIFE COMPONENT - TYPESCRIPT
// ========================================

// src/app/components/campus-life/campus-life.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { 
  LucideAngularModule, 
  BookOpen, Award, Star, Users, CheckCircle 
} from 'lucide-angular';

@Component({
  selector: 'app-campus-life',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './campus-life.component.html',
  styleUrls: ['./campus-life.component.css']
})
export class CampusLifeComponent {
  // Register icons
  readonly BookOpen = BookOpen;
  readonly Award = Award;
  readonly Star = Star;
  readonly Users = Users;
  readonly CheckCircle = CheckCircle;

  // Student organization categories
  organizationCategories = [
    { name: 'Academic Clubs', count: '50+', icon: BookOpen, color: 'blue' },
    { name: 'Sports Teams', count: '30+', icon: Award, color: 'green' },
    { name: 'Arts & Culture', count: '40+', icon: Star, color: 'purple' },
    { name: 'Community Service', count: '35+', icon: Users, color: 'orange' }
  ];

  // Campus facilities
  facilities = [
    { 
      title: 'Modern Dormitories', 
      desc: 'Comfortable on-campus housing with Wi-Fi, study lounges, and recreational facilities', 
      image: 'https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=400',
      features: ['Single & Double Rooms', '24/7 Security', 'Laundry Facilities']
    },
    { 
      title: 'Fitness & Recreation', 
      desc: 'State-of-the-art gym with Olympic pool, basketball courts, and training facilities', 
      image: 'https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=400',
      features: ['Gym & Pool', 'Sports Courts', 'Yoga Studio']
    },
    { 
      title: 'Student Center Hub', 
      desc: 'Central hub for dining, events, student services, and social activities', 
      image: 'https://images.unsplash.com/photo-1541339907198-e08756dedf3f?w=400',
      features: ['Dining Options', 'Event Spaces', 'Study Areas']
    }
  ];

  // Student testimonials
  testimonials = [
    { 
      name: 'Sarah Johnson', 
      major: 'Computer Science, Class of 2025', 
      quote: 'The research opportunities and supportive community here have been absolutely incredible. I\'ve grown both academically and personally.',
      rating: 5
    },
    { 
      name: 'Michael Chen', 
      major: 'Business Administration, Class of 2024', 
      quote: 'Excellence University prepared me perfectly for my career. The internship connections and mentorship programs are unmatched.',
      rating: 5
    },
    { 
      name: 'Emily Rodriguez', 
      major: 'Medicine, Class of 2026', 
      quote: 'The hands-on clinical experience and world-class faculty make this medical program one of the best. I couldn\'t be happier with my choice.',
      rating: 5
    }
  ];

  // Dining options
  diningOptions = [
    { name: 'Main Dining Hall', type: 'All-You-Can-Eat', hours: '7 AM - 9 PM', cuisine: 'International' },
    { name: 'Coffee House', type: 'Café', hours: '6 AM - 11 PM', cuisine: 'Coffee & Pastries' },
    { name: 'Food Court', type: 'Multiple Vendors', hours: '10 AM - 10 PM', cuisine: 'Various' },
    { name: 'Healthy Bites', type: 'Vegetarian/Vegan', hours: '11 AM - 8 PM', cuisine: 'Plant-Based' }
  ];

  // Campus events
  campusEvents = [
    { name: 'Welcome Week', description: 'Orientation activities for new students', frequency: 'Annually' },
    { name: 'Cultural Festival', description: 'Celebrate diversity with food, performances, and art', frequency: 'Semester' },
    { name: 'Homecoming', description: 'Alumni reunion with games, concerts, and activities', frequency: 'Annually' },
    { name: 'Spring Concert', description: 'Live music performances by popular artists', frequency: 'Annually' },
    { name: 'Career Fair', description: 'Connect with employers and explore opportunities', frequency: 'Semester' },
    { name: 'Club Rush', description: 'Join student organizations and meet new people', frequency: 'Semester' }
  ];

  getInitials(name: string): string {
    return name.split(' ').map(n => n[0]).join('');
  }

  getStarArray(rating: number): number[] {
    return Array(rating).fill(0);
  }
}