import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ManifestDocumentsComponent} from './manifest-documents.component';

describe('ManifestDocumentsComponent', () => {
  let component: ManifestDocumentsComponent;
  let fixture: ComponentFixture<ManifestDocumentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManifestDocumentsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManifestDocumentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
