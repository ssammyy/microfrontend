import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ViewRfcCocDocumentsComponent} from './view-rfc-coc-documents.component';

describe('ViewRfcCocDocumentsComponent', () => {
  let component: ViewRfcCocDocumentsComponent;
  let fixture: ComponentFixture<ViewRfcCocDocumentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewRfcCocDocumentsComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewRfcCocDocumentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
