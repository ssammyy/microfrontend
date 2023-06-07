import {ComponentFixture, TestBed} from '@angular/core/testing';

import {PvocQueriesComponent} from './pvoc-queries.component';

describe('PvocQueriesComponent', () => {
  let component: PvocQueriesComponent;
  let fixture: ComponentFixture<PvocQueriesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PvocQueriesComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PvocQueriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
