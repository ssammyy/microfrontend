import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewIdfDocumentDetailsComponent } from './view-idf-document-details.component';

describe('ViewIdfDocumentDetailsComponent', () => {
  let component: ViewIdfDocumentDetailsComponent;
  let fixture: ComponentFixture<ViewIdfDocumentDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewIdfDocumentDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewIdfDocumentDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
