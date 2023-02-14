import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DisseminationPublicationComponent } from './dissemination-publication.component';

describe('DisseminationPublicationComponent', () => {
  let component: DisseminationPublicationComponent;
  let fixture: ComponentFixture<DisseminationPublicationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DisseminationPublicationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DisseminationPublicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
