import {ComponentFixture, TestBed} from '@angular/core/testing';

import {IncompleteIDFDocumentsComponent} from './incomplete-idfdocuments.component';

describe('IncompleteIDFDocumentsComponent', () => {
  let component: IncompleteIDFDocumentsComponent;
  let fixture: ComponentFixture<IncompleteIDFDocumentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [IncompleteIDFDocumentsComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IncompleteIDFDocumentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
