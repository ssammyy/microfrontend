import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PermitsDeferredComponent } from './permits-deferred.component';

describe('PermitsDeferredComponent', () => {
  let component: PermitsDeferredComponent;
  let fixture: ComponentFixture<PermitsDeferredComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PermitsDeferredComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PermitsDeferredComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
