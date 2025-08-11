#!/bin/bash

# VFD Docker Startup Script
# This script starts all VFD services for frontend testing

echo "🚀 Starting VFD Services for Frontend Testing..."

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker first."
    exit 1
fi

# Check if Docker Compose is available
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

echo "📦 Building VFD service..."
docker-compose build vfd-service

echo "🔄 Starting all services..."
docker-compose up -d

echo "⏳ Waiting for services to be ready..."
sleep 10

# Check service status
echo "📊 Service Status:"
docker-compose ps

echo ""
echo "✅ VFD Services are starting up!"
echo ""
echo "🌐 Service URLs:"
echo "   VFD API: http://localhost:8085"
echo "   Swagger UI: http://localhost:8080"
echo "   pgAdmin: http://localhost:5050 (admin@vfd.local / admin123)"
echo "   PostgreSQL: localhost:5432"
echo "   Redis: localhost:6379"
echo ""
echo "📋 Useful Commands:"
echo "   View logs: docker-compose logs -f"
echo "   Stop services: docker-compose down"
echo "   Restart services: docker-compose restart"
echo "   View service status: docker-compose ps"
echo ""
echo "🔍 Health Checks:"
echo "   VFD Service: http://localhost:8085/vfd/actuator/health"
echo "   Database: docker-compose exec postgres pg_isready -U vfd_user -d vfd_db"
echo ""
echo "🎯 Ready for frontend testing!"
