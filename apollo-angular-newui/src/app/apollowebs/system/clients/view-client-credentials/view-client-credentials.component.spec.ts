import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewClientCredentialsComponent } from './view-client-credentials.component';

describe('ViewClientCredentialsComponent', () => {
  let component: ViewClientCredentialsComponent;
  let fixture: ComponentFixture<ViewClientCredentialsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewClientCredentialsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewClientCredentialsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
