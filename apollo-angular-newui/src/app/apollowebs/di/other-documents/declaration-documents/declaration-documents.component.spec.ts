import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DeclarationDocumentsComponent} from './declaration-documents.component';

describe('DeclarationDocumentsComponent', () => {
  let component: DeclarationDocumentsComponent;
  let fixture: ComponentFixture<DeclarationDocumentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DeclarationDocumentsComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeclarationDocumentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
