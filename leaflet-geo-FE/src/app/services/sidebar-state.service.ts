import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SidebarStateService {
  private sidebarTypeSubject = new BehaviorSubject<'main' | 'thematic'>('main');
  sidebarType$: Observable<'main' | 'thematic'> = this.sidebarTypeSubject.asObservable();

  constructor() {}

  setSidebarType(type: 'main' | 'thematic') {
    this.sidebarTypeSubject.next(type);
  }

  getSidebarType(): 'main' | 'thematic' {
    return this.sidebarTypeSubject.getValue();
  }
}
