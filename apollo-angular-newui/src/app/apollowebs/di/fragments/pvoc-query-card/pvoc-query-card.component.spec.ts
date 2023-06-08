import {ComponentFixture, TestBed} from '@angular/core/testing';

import {PvocQueryCardComponent} from './pvoc-query-card.component';

describe('PvocQueryCardComponent', () => {
  let component: PvocQueryCardComponent;
  let fixture: ComponentFixture<PvocQueryCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PvocQueryCardComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PvocQueryCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
