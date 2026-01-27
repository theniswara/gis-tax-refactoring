import { Component, OnInit, OnDestroy } from '@angular/core';
import { EventService } from '../../../services/event.service';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { getSidebarSize } from 'src/app/store/layouts/layout-selector';
import { RootReducerState } from 'src/app/store';
import { Store } from '@ngrx/store';
import { SidebarStateService } from '../../../services/sidebar-state.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-vertical',
  templateUrl: './vertical.component.html',
  styleUrls: ['./vertical.component.scss']
})
export class VerticalComponent implements OnInit, OnDestroy {

  isCondensed = false;
  getsize: any;
  sidebarType: 'main' | 'thematic' = 'main';
  private destroy$ = new Subject<void>();

  constructor(
    private eventService: EventService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private store: Store<RootReducerState>,
    private sidebarStateService: SidebarStateService
  ) {
  }

  ngOnInit(): void {
    // Subscribe to sidebar state changes
    this.sidebarStateService.sidebarType$
      .pipe(takeUntil(this.destroy$))
      .subscribe(type => {
        this.sidebarType = type;
      });

    // Check route on navigation
    this.router.events
      .pipe(takeUntil(this.destroy$))
      .subscribe((event: any) => {
        if (event instanceof NavigationEnd) {
          // Check if we're on tematik route
          const isTematikRoute = event.url.includes('/tematik');
          if (isTematikRoute) {
            this.sidebarStateService.setSidebarType('thematic');
          } else {
            this.sidebarStateService.setSidebarType('main');
          }

          if (document.documentElement.getAttribute('data-preloader') == 'enable') {
            const preloader = document.getElementById("preloader");
            if (!preloader) return;
            if (event.url !== '/disabled-route') {
              preloader.style.opacity = "1";
              preloader.style.visibility = "";
              setTimeout(() => {
                if (preloader) {
                  preloader.style.opacity = "0";
                  preloader.style.visibility = "hidden";
                }
              }, 1000);
            } else {
              preloader.style.opacity = "0";
              preloader.style.visibility = "hidden";
            }
          }
        }
      });

    this.handlePreloader(this.activatedRoute.snapshot.routeConfig?.path);
    if (document.documentElement.getAttribute('data-sidebar-size') == 'lg') {
      this.store.select(getSidebarSize).subscribe((size) => {
        this.getsize = size
      })
      window.addEventListener('resize', () => {
        var self = this;
        if (document.documentElement.clientWidth <= 767) {
          document.documentElement.setAttribute('data-sidebar-size', '');
          document.querySelector('.hamburger-icon')?.classList.add('open')
        }
        else if (document.documentElement.clientWidth <= 1024) {
          document.documentElement.setAttribute('data-sidebar-size', 'sm');
          document.querySelector('.hamburger-icon')?.classList.add('open')
        }
        else if (document.documentElement.clientWidth >= 1024) {
          if (document.documentElement.getAttribute('data-layout-width') == 'fluid') {
            document.documentElement.setAttribute('data-sidebar-size', self.getsize);
            document.querySelector('.hamburger-icon')?.classList.remove('open')
          }
        }
      })
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private handlePreloader(route: any) {
    const preloader = document.getElementById("preloader");
    if (!preloader) return;
    if (route !== '/disabled-route') {
      preloader.style.opacity = "1";
      preloader.style.visibility = "";
      setTimeout(() => {
        if (preloader) {
          preloader.style.opacity = "0";
          preloader.style.visibility = "hidden";
        }
      }, 1000);
    } else {
      preloader.style.opacity = "0";
      preloader.style.visibility = "hidden";
    }
  }

  /**
   * On mobile toggle button clicked
   */
  onToggleMobileMenu() {
    const currentSIdebarSize = document.documentElement.getAttribute("data-sidebar-size");
    if (document.documentElement.clientWidth >= 767) {
      if (currentSIdebarSize == null) {
        (document.documentElement.getAttribute('data-sidebar-size') == null || document.documentElement.getAttribute('data-sidebar-size') == "lg") ? document.documentElement.setAttribute('data-sidebar-size', 'sm') : document.documentElement.setAttribute('data-sidebar-size', 'lg')
      } else if (currentSIdebarSize == "md") {
        (document.documentElement.getAttribute('data-sidebar-size') == "md") ? document.documentElement.setAttribute('data-sidebar-size', 'sm') : document.documentElement.setAttribute('data-sidebar-size', 'md')
      } else {
        (document.documentElement.getAttribute('data-sidebar-size') == "sm") ? document.documentElement.setAttribute('data-sidebar-size', 'lg') : document.documentElement.setAttribute('data-sidebar-size', 'sm')
      }
    }

    if (document.documentElement.clientWidth <= 767) {
      document.body.classList.toggle('vertical-sidebar-enable');
    }
    this.isCondensed = !this.isCondensed;
  }

  /**
   * on settings button clicked from topbar
   */
  onSettingsButtonClicked() {
    document.body.classList.toggle('right-bar-enabled');
    const rightBar = document.getElementById('theme-settings-offcanvas');
    if (rightBar != null) {
      rightBar.classList.toggle('show');
      rightBar.setAttribute('style', "visibility: visible;");

    }
  }

  onResize(event: any) {
    if (document.body.getAttribute('layout') == "twocolumn") {
      if (event.target.innerWidth <= 767) {
        this.eventService.broadcast('changeLayout', 'vertical');
      } else {
        this.eventService.broadcast('changeLayout', 'twocolumn');
        document.body.classList.remove('twocolumn-panel');
        document.body.classList.remove('vertical-sidebar-enable');
      }
    }
  }

  /**
   * Handle actions emitted from thematic sidebar
   */
  onThematicAction(action: any) {
    console.log('Thematic action:', action);
    // Handle specific thematic actions here if needed
    // For example, open modals, navigate to specific themes, etc.
  }

}
