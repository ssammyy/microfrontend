import {ComponentFixture, TestBed} from '@angular/core/testing';

import {PvocCocQueriesComponent} from './pvoc-coc-queries.component';

describe('PvocCocQueriesComponent', () => {
  let component: PvocCocQueriesComponent;
  let fixture: ComponentFixture<PvocCocQueriesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PvocCocQueriesComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PvocCocQueriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
