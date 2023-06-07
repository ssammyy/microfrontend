import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ManifestDocumentComponent} from './manifest-document.component';

describe('ManifestDocumentComponent', () => {
  let component: ManifestDocumentComponent;
  let fixture: ComponentFixture<ManifestDocumentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ManifestDocumentComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManifestDocumentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
