import {Component, EventEmitter, HostListener, OnInit, Output} from '@angular/core';
import {navBarData} from "./nav-data";
import { ActivatedRoute } from '@angular/router';
interface SideNavToggle{
  screenWidht: number;
  collapsed: boolean;
}
@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.css']
})
export class SideBarComponent implements OnInit{
  @Output() onToggleSideNav: EventEmitter<SideNavToggle> = new EventEmitter()
  collapsed = false;
  screenWidth= 0;
  navData = navBarData

  @HostListener('window:resize', ['$event'])
  onResize(event: any){
    this.screenWidth = window.innerWidth
    if (this.screenWidth <= 768){
      this.collapsed= false
      this.onToggleSideNav.emit({collapsed: this.collapsed, screenWidht: this.screenWidth})
    }

  }
  ngOnInit() {
    this.screenWidth= window.innerWidth
  }

  toggleCollapse(): void{
    this.collapsed =!this.collapsed;
    this.onToggleSideNav.emit({collapsed: this.collapsed, screenWidht: this.screenWidth})

  }
  closeSideNav() : void{
    this.collapsed= false
    this.onToggleSideNav.emit({collapsed: this.collapsed, screenWidht: this.screenWidth})

  }

}
