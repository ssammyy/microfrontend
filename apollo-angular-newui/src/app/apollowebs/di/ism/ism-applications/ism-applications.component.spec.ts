import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IsmApplicationsComponent } from './ism-applications.component';

describe('IsmApplicationsComponent', () => {
  let component: IsmApplicationsComponent;
  let fixture: ComponentFixture<IsmApplicationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IsmApplicationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IsmApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
