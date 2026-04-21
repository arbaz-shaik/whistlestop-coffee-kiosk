# Whistlestop Coffee Kiosk - Team 07

Mobile coffee ordering system for Cramlington Station (CSC8019).

## Team Structure
- **Shaik Arbaz** - Backend Developer (Status & Staff) + Project Lead
- **Parth** - Backend Developer (Menu & Order)
- **Person A** - UI/UX Designer
- **Person B** - Frontend Developer
- **Person E** - Framework Integration
- **Person F** - Database Developer

## Tech Stack
- **Backend:** Java + Spring Boot + MySQL
- **Frontend:** React + Ant Design
- **Design:** Figma
- **Version Control:** Git + GitHub

## Repository Rules
- `main` branch is **PROTECTED**
- All work done in feature branches
- Pull Requests reviewed by Arbaz before merge
- No direct commits to main

## Branch Strategy
main (protected - Arbaz controls)
↑
└── feature/status (Arbaz)
└── feature/staff (Arbaz)
└── feature/menu (Parth)
└── feature/order (Parth)
└── feature/frontend (Person A/B)
└── feature/database (Person F)
└── feature/framework (Person E)

## Workflow for Team Members
1. Clone repository
2. Create your feature branch from `main`
3. Work in your branch
4. Commit and push to YOUR branch
5. Open Pull Request to `main`
6. Wait for Arbaz's review and approval
7. Arbaz merges

## Project Structure
whistlestop-coffee-kiosk/
├── docs/              # Documentation
│   ├── proposals/     # Individual module proposals
│   └── meeting-notes/ # Team meeting notes
├── frontend/          # React application
├── backend/           # Spring Boot application
└── database/          # SQL schemas and scripts

## Timeline
- **By March 13:** Menu page complete
- **Week 3-4:** Order placement
- **Week 5-6:** Staff dashboard
- **Week 7-8:** Extensions
- **Week 9:** Testing
- **Week 10:** Documentation

## Opening Hours
- **Monday-Friday:** 06:30-19:00
- **Saturday:** 07:00-18:00
- **Sunday:** Closed

## Getting Started

### Clone Repository
```bash
git clone https://github.com/arbaz-shaik/whistlestop-coffee-kiosk.git
cd whistlestop-coffee-kiosk
```

### Team Members: Create Your Feature Branch
```bash
# Create your branch from main
git checkout -b feature/your-name-your-module

# Work and commit
git add .
git commit -m "Your changes"
git push origin feature/your-name-your-module

# Then open Pull Request on GitHub
```

## Project Lead (Arbaz)
- Reviews all Pull Requests
- Merges approved PRs to main
- Maintains main branch quality
