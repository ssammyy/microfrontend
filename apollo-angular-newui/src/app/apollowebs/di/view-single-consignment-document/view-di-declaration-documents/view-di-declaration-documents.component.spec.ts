import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewDiDeclarationDocumentsComponent } from './view-di-declaration-documents.component';

describe('ViewDiDeclarationDocumentsComponent', () => {
  let component: ViewDiDeclarationDocumentsComponent;
  let fixture: ComponentFixture<ViewDiDeclarationDocumentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewDiDeclarationDocumentsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewDiDeclarationDocumentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
