import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComStdPublishingComponent } from './com-std-publishing.component';

describe('ComStdPublishingComponent', () => {
  let component: ComStdPublishingComponent;
  let fixture: ComponentFixture<ComStdPublishingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComStdPublishingComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComStdPublishingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
