import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EpraBatchNewComponent } from './epra-batch-new.component';

describe('EpraBatchNewComponent', () => {
  let component: EpraBatchNewComponent;
  let fixture: ComponentFixture<EpraBatchNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EpraBatchNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EpraBatchNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
