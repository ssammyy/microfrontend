import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublishingEnquiriestComponent } from './publishing-enquiriest.component';

describe('PublishingEnquiriestComponent', () => {
  let component: PublishingEnquiriestComponent;
  let fixture: ComponentFixture<PublishingEnquiriestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PublishingEnquiriestComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PublishingEnquiriestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
