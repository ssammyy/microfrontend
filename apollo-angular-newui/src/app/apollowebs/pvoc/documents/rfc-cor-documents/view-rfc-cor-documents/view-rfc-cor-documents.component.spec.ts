import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ViewRfcCorDocumentsComponent} from './view-rfc-cor-documents.component';

describe('ViewRfcCorDocumentsComponent', () => {
  let component: ViewRfcCorDocumentsComponent;
  let fixture: ComponentFixture<ViewRfcCorDocumentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewRfcCorDocumentsComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewRfcCorDocumentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
