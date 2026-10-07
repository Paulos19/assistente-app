# Product

<!-- impeccable:product-schema 1 -->

## Platform

android

## Users
Paulo Henrique — developer and admin who manages his own hybrid infrastructure (VPS on Easypanel + personal Windows PC) remotely from his phone. Secondary: friends and family who use the media downloader as guests.

## Product Purpose
Agente is a personal command center that connects to a FastAPI backend running on a VPS, providing three capabilities from any Android device: real-time monitoring and control of hybrid infrastructure (VPS + home PC via WebSocket tunnel), high-fidelity media extraction from YouTube/Instagram/TikTok with embedded artwork and ID3 tags, and a conversational interface to a Gemini-powered DevOps agent that can execute terminal commands, manage files, and schedule tasks.

## Positioning
A single-user operations dashboard that bridges cloud infrastructure and a personal desktop through a WebSocket tunnel, controlled from WhatsApp, Telegram, or this dedicated native app. The media downloader converts social links into properly tagged, artwork-embedded audio files saved directly to the phone's Music folder via Android's native DownloadManager.

## Operating Context
Used on the go, often on mobile data, to check VPS health, trigger PC actions (screenshot, lock, suspend), download music for offline listening, or send quick commands to the AI agent. The backend is always-on at agent.phdev.top; the Windows worker connects intermittently.

## Capabilities and Constraints
- Login via phone number matched against a server-side whitelist (admin vs guest roles).
- Admin: full dashboard (CPU/RAM metrics, PC connectivity), media downloader, and agent chat.
- Guest: media downloader only.
- Auth token stored in memory (not yet persisted via DataStore).
- Agent chat currently simulated locally; real integration pending.
- No Java SDK installed on development machine; builds run via GitHub Actions CI.

## Brand Commitments
- Name: **Agente**
- Visual direction: glassmorphism, translucent pastels, soft gradients — ethereal and light.
- Logo style: minimalist, geometric, abstract.

## Evidence on Hand
- Working backend with live APIs at agent.phdev.top.
- Existing app at version 1.1.2 with functional login, admin dashboard, and media downloader.
- GitHub Actions CI producing debug APKs.

## Product Principles
1. **One-thumb control** — every critical action reachable from a phone held in one hand.
2. **Ambient awareness** — infrastructure status visible at a glance without drill-down.
3. **Invisible complexity** — the WebSocket tunnel, ID3 tagging, and AI orchestration stay behind simple surfaces.
4. **Personal, not enterprise** — this is a power user's private tool, not a team dashboard.

## Accessibility & Inclusion
Standard Material 3 accessibility: touch targets ≥48dp, contrast ratios met, system font scaling respected.
