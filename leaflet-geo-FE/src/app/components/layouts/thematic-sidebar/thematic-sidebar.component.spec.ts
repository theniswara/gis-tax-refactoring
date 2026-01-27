import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ThematicSidebarComponent } from './thematic-sidebar.component';

describe('ThematicSidebarComponent', () => {
  let component: ThematicSidebarComponent;
  let fixture: ComponentFixture<ThematicSidebarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ThematicSidebarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ThematicSidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
