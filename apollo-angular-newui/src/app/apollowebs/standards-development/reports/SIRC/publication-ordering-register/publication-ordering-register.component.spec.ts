import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicationOrderingRegisterComponent } from './publication-ordering-register.component';

describe('PublicationOrderingRegisterComponent', () => {
  let component: PublicationOrderingRegisterComponent;
  let fixture: ComponentFixture<PublicationOrderingRegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PublicationOrderingRegisterComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PublicationOrderingRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
