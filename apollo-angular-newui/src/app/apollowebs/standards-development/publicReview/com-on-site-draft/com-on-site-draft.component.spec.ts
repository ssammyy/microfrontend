import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComOnSiteDraftComponent } from './com-on-site-draft.component';

describe('ComOnSiteDraftComponent', () => {
  let component: ComOnSiteDraftComponent;
  let fixture: ComponentFixture<ComOnSiteDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComOnSiteDraftComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComOnSiteDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
