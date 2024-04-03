import {Component, OnInit} from '@angular/core';
import {NavigationEnd, Router} from "@angular/router";

interface SideNavToggle{
  screenWidht: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit{
  title = 'Inventory';
  isSideNavCollapsed = false;
  screenWidth = 0;
  showSideBar = true

  constructor(private router: Router) {}


  ngOnInit() {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.showSideBar = !event.url.includes('login');
      }
    });
  }

  onToggleSideNav(data: SideNavToggle): void{
    this.screenWidth = data.screenWidht
    this.isSideNavCollapsed = data.collapsed

  }
}
