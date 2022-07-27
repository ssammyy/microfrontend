import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ViewOtherDocumentsComponent} from './view-other-documents.component';

describe('ViewOtherDocumentsComponent', () => {
  let component: ViewOtherDocumentsComponent;
  let fixture: ComponentFixture<ViewOtherDocumentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewOtherDocumentsComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewOtherDocumentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
