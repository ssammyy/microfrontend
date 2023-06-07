import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RfcCocDocumentsComponent} from './rfc-coc-documents.component';

describe('RfcCocDocumentsComponent', () => {
  let component: RfcCocDocumentsComponent;
  let fixture: ComponentFixture<RfcCocDocumentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RfcCocDocumentsComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RfcCocDocumentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
