/*
import { Component } from '@angular/core';

@Component({
  selector: 'app-notification',
  standalone: true,
  imports: [],
  templateUrl: './notification.component.html',
  styleUrl: './notification.component.css'
})
export class NotificationComponent {

}
*/

// src/app/components/notification/notification.component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subject, takeUntil } from 'rxjs';
import { LucideAngularModule, CheckCircle, XCircle, Info, AlertTriangle } from 'lucide-angular';
import { NotificationService, Notification } from '../../services/notification.service';

@Component({
  selector: 'app-notification',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  template: `
    <div *ngIf="notification" 
         [class]="getNotificationClass()"
         class="fixed top-24 right-4 px-6 py-3 rounded-lg shadow-lg z-50 animate-fade-in flex items-center">
      <lucide-icon [img]="getIcon()" [size]="20" class="mr-2"></lucide-icon>
      {{ notification.message }}
    </div>
  `,
  styles: [`
    @keyframes fade-in {
      from { opacity: 0; transform: translateY(-10px); }
      to { opacity: 1; transform: translateY(0); }
    }
    .animate-fade-in {
      animation: fade-in 0.3s ease-out;
    }
  `]
})
export class NotificationComponent implements OnInit, OnDestroy {
  readonly CheckCircle = CheckCircle;
  readonly XCircle = XCircle;
  readonly Info = Info;
  readonly AlertTriangle = AlertTriangle;

  notification: Notification | null = null;
  private destroy$ = new Subject<void>();

  constructor(private notificationService: NotificationService) {}

  ngOnInit(): void {
    this.notificationService.notification$
      .pipe(takeUntil(this.destroy$))
      .subscribe(notification => {
        this.notification = notification;
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  getNotificationClass(): string {
    if (!this.notification) return '';
    
    const baseClasses = 'text-white ';
    const typeClasses = {
      success: 'bg-green-500',
      error: 'bg-red-500',
      info: 'bg-blue-500',
      warning: 'bg-yellow-500'
    };
    
    return baseClasses + typeClasses[this.notification.type];
  }

  getIcon(): any {
    if (!this.notification) return this.Info;
    
    const icons = {
      success: this.CheckCircle,
      error: this.XCircle,
      info: this.Info,
      warning: this.AlertTriangle
    };
    
    return icons[this.notification.type];
  }
}
