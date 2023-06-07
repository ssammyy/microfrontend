import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddApiClientComponent } from './add-api-client.component';

describe('AddApiClientComponent', () => {
  let component: AddApiClientComponent;
  let fixture: ComponentFixture<AddApiClientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddApiClientComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddApiClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
