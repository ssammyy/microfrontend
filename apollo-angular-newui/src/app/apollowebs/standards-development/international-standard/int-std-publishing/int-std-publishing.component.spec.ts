import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdPublishingComponent } from './int-std-publishing.component';

describe('IntStdPublishingComponent', () => {
  let component: IntStdPublishingComponent;
  let fixture: ComponentFixture<IntStdPublishingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdPublishingComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdPublishingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
