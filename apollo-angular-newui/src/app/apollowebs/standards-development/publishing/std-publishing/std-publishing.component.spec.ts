import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StdPublishingComponent } from './std-publishing.component';

describe('StdPublishingComponent', () => {
  let component: StdPublishingComponent;
  let fixture: ComponentFixture<StdPublishingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StdPublishingComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StdPublishingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
