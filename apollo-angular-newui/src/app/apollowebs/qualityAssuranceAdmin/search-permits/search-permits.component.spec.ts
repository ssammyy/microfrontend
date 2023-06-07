import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchPermitsComponent } from './search-permits.component';

describe('SearchPermitsComponent', () => {
  let component: SearchPermitsComponent;
  let fixture: ComponentFixture<SearchPermitsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchPermitsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchPermitsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
