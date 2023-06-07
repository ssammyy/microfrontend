import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewDeclarationDocumentsItemDetailsListComponent } from './view-declaration-documents-item-details-list.component';

describe('ViewDeclarationDocumentsItemDetailsListComponent', () => {
  let component: ViewDeclarationDocumentsItemDetailsListComponent;
  let fixture: ComponentFixture<ViewDeclarationDocumentsItemDetailsListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewDeclarationDocumentsItemDetailsListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewDeclarationDocumentsItemDetailsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
