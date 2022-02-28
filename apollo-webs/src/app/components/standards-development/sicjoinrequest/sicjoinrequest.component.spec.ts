import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SicjoinrequestComponent } from './sicjoinrequest.component';

describe('SicjoinrequestComponent', () => {
  let component: SicjoinrequestComponent;
  let fixture: ComponentFixture<SicjoinrequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SicjoinrequestComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SicjoinrequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
