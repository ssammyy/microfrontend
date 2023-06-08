import {ComponentFixture, TestBed} from '@angular/core/testing';

import {PvocQueryViewComponent} from './pvoc-query-view.component';

describe('PvocQueryViewComponent', () => {
  let component: PvocQueryViewComponent;
  let fixture: ComponentFixture<PvocQueryViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PvocQueryViewComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PvocQueryViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
