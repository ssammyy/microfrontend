import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RfcCorDocumentsComponent} from './rfc-cor-documents.component';

describe('RfcCorDocumentsComponent', () => {
  let component: RfcCorDocumentsComponent;
  let fixture: ComponentFixture<RfcCorDocumentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RfcCorDocumentsComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RfcCorDocumentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
